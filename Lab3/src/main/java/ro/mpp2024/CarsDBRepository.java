package ro.mpp2024;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements CarRepository{

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public CarsDBRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public List<Car> findByManufacturer(String manufacturerN) {
        logger.info("Finding Cars by Manufacturer "+manufacturerN);
        List<Car> cars=new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("select * from cars where manufacturer=?")){
            preparedStatement.setString(1,manufacturerN);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String manufacturer = resultSet.getString("manufacturer");
                    String model = resultSet.getString("model");
                    int year = resultSet.getInt("year");
                    Car car  = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        }
        catch (SQLException e){
            logger.error(e);
            System.err.println("Error while connecting to DB");
        }
        return cars;
    }

    @Override
    public List<Car> findBetweenYears(int min, int max) {
        logger.info("Finding Cars by Years ");
        List<Car> cars=new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("select * from cars where year >=? and year <=?")){
            preparedStatement.setInt(1, min);
            preparedStatement.setInt(2, max);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String manufacturer = resultSet.getString("manufacturer");
                    String model = resultSet.getString("model");
                    int year = resultSet.getInt("year");
                    Car car  = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        }
        catch (SQLException e){
            logger.error(e);
            System.err.println("Error while connecting to DB");
        }
        return cars;
    }

    @Override
    public void add(Car elem) {
        logger.traceEntry("Saving task {}" , elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into cars(manufacturer, model, year) values(?,?,?)") ) {
            preStmt.setString(1, elem.getManufacturer());
            preStmt.setString(2, elem.getModel());
            preStmt.setInt(3, elem.getYear());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances" , result);
        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Car elem) {
        logger.traceEntry("Updating element {}" , elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("update cars set manufacturer = ?, model = ?, year = ? where id = ?")){
            preparedStatement.setString(1, elem.getManufacturer());
            preparedStatement.setString(2, elem.getModel());
            preparedStatement.setInt(3, elem.getYear());
            preparedStatement.setInt(4, integer);
            int result = preparedStatement.executeUpdate();
            logger.trace("Updating {} instances" , result);
        }
        catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit();
    }

    @Override
    public Iterable<Car> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from cars")) {
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");
                    Car car  = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }

        } catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(cars);
        return cars;
    }
}
