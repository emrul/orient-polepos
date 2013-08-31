package org.polepos.teams.orient;

import com.orientechnologies.orient.core.OConstants;
import org.polepos.framework.Car;
import org.polepos.framework.ConfigurationSetting;
import org.polepos.framework.DriverBase;
import org.polepos.framework.Team;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class OrientTeam extends Team {
	
	private String _color;
    
    public String _name = orientName();
    
    private final List<DriverBase> _drivers;

	private int[] _options;
	
	private ConfigurationSetting[] _configurations;
	
	private Car[] _cars;
	
    public OrientTeam(boolean loadDrivers) {
        _drivers = new ArrayList<DriverBase>();
        if(loadDrivers) {
        	addDrivers();
        }
    }

    public OrientTeam() {
    	this(true);
    }

    private void addDrivers(){
    	addDriver(new ComplexOrient());
    	addDriver(new InheritanceHierarchyOrient());
    	addDriver(new NestedListsOrient());
    	addDriver(new FlatObjectOrient());
        addDriver(new TreesOrient());
        addDriver(new NativeIdsOrient());
        addDriver(new CommitsOrient());
        addDriver(new StringsOrient());
        addDriver(new ArrayListsOrient());
    	addDriver(new ComplexConcurrencyOrient());
    }
    
    @Override
    public String name(){
		return _name;
	}
    
    @Override
    public String description() {
        return "???";
    }
    
    

    @Override
    public Car[] cars(){
    	if(_cars == null){
    		_cars = new Car[]{ new OrientCar(this, _options, _configurations) };
    	}
		return _cars;
	}
    
    public void addDriver(DriverBase driver){
        _drivers.add(driver);
    }
    
    public void addDriver(String driverName){
        try {
            Class<?> clazz = this.getClass().getClassLoader().loadClass(driverName);
            Constructor<?> constr = clazz.getConstructor();
            addDriver((DriverBase)constr.newInstance());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public DriverBase[] drivers() {
        return _drivers.toArray(new DriverBase[_drivers.size()]);
    }

    @Override
    public String website() {
        return OConstants.ORIENT_URL;
    }

    @Override
    public void configure(int[] options, ConfigurationSetting[] configurations) {
    	_options = options;
    	_configurations = configurations;
        _name = orientName();
        
        if(configurations != null){
        	for (int i = 0; i < configurations.length; i++) {
        		_name += " " + configurations[i].name();
			}
        }
        
        if(options != null){
            for (int i = 0; i < options.length; i++) {
                try{
                    switch (options[i]){
                        case OrientOptions.STORAGE_LOCAL:
                            _name += " LOCAL";
                            break;
                        case OrientOptions.STORAGE_MEMORY:
                            _name += " MEMORY";
                            break;
                        case OrientOptions.EMBEDDED_SERVER:
                            _name += " EMBEDDED";
                            break;
                        default:
                            break;
                    }
                }catch (Throwable t){
                    System.err.println("orient option not available in this version");
                    t.printStackTrace();
                }
            }
        }
    }
    
    private String orientName(){
        return "orient";
    }

	public void setUp() {
		new File(OrientCar.FOLDER).mkdirs();
	    try {
	    	defaultCar().deleteDatabaseFile(); 
		} 
	    catch (IOException e) {
	    	throw new RuntimeException(e);
		}
	}

	private OrientCar defaultCar() {
		Car car = cars()[0];
		return (OrientCar) car;
	}

	protected void tearDown() {
		defaultCar().stopServer();
	}
    
	public final String databaseFile(){
        return defaultCar().databaseFile();
    }

    public int colorFor(Car car) {
    	if(_color == null){
    		return super.colorFor(car);
    	}
		if (_color.startsWith("0x")) {
			_color = _color.substring(2);
		}
		return Integer.parseInt(_color, 16);
    }
    
    public void setColor(String color){
    	_color = color;
    }
    
}
