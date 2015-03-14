package com.superapp.info;

import java.io.Serializable;

public class GongdanInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4442632783466813398L;

	String id;
	
	String dev_id;
	String receiver;
	String receive_time;
	String info_source;
	String task_type;
	String task_id;
	String task_name;
	String order;
	String location;
	String event_time;
	String pic_path_down;
	String vehicle_fault_num;
	String vehicle_fault_type;
	String parking_position;
	String cargo;
	String wrecker_num;
	String wrecker_type;
	public String getTxPath() {
		return TxPath;
	}
	public void setTxPath(String txPath) {
		TxPath = txPath;
	}
	String driver;
	String co_driver;
	String state;
	String TxPath;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDev_id() {
		return dev_id;
	}
	public void setDev_id(String dev_id) {
		this.dev_id = dev_id;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getReceive_time() {
		return receive_time;
	}
	public void setReceive_time(String receive_time) {
		this.receive_time = receive_time;
	}
	public String getInfo_source() {
		return info_source;
	}
	public void setInfo_source(String info_source) {
		this.info_source = info_source;
	}
	public String getTask_type() {
		return task_type;
	}
	public void setTask_type(String task_type) {
		this.task_type = task_type;
	}
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getEvent_time() {
		return event_time;
	}
	public void setEvent_time(String event_time) {
		this.event_time = event_time;
	}
	public String getPic_path_down() {
		return pic_path_down;
	}
	public void setPic_path_down(String pic_path_down) {
		this.pic_path_down = pic_path_down;
	}
	public String getVehicle_fault_num() {
		return vehicle_fault_num;
	}
	public void setVehicle_fault_num(String vehicle_fault_num) {
		this.vehicle_fault_num = vehicle_fault_num;
	}
	public String getVehicle_fault_type() {
		return vehicle_fault_type;
	}
	public void setVehicle_fault_type(String vehicle_fault_type) {
		this.vehicle_fault_type = vehicle_fault_type;
	}
	public String getParking_position() {
		return parking_position;
	}
	public void setParking_position(String parking_position) {
		this.parking_position = parking_position;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getWrecker_num() {
		return wrecker_num;
	}
	public void setWrecker_num(String wrecker_num) {
		this.wrecker_num = wrecker_num;
	}
	public String getWrecker_type() {
		return wrecker_type;
	}
	public void setWrecker_type(String wrecker_type) {
		this.wrecker_type = wrecker_type;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getCo_driver() {
		return co_driver;
	}
	public void setCo_driver(String co_driver) {
		this.co_driver = co_driver;
	}
	
	
	
	
	
	
	

}
