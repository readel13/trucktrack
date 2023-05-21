alter table work_trip_cargo_history
 add loading_location varchar(50),
 add loading_time timestamp,
 add unloading_location varchar(50),
 add unloading_time timestamp;