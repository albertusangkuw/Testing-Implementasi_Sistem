package controller

import (
	"database/sql"
	"log"

	_ "github.com/go-sql-driver/mysql"
)

var db *sql.DB

func connect() *sql.DB {
	//db, err := sql.Open("mysql", "apiemusic:XQ9YqNldKL9ANO15@tcp(localhost:3306)/db_emusic_android?parseTime=true&loc=Asia%2FJakarta")
	var err error
	db, err = sql.Open("mysql", "root:@tcp(localhost:3306)/db_emusic?parseTime=true&loc=Asia%2FJakarta")
	if err != nil {
		log.Fatal(err)
	}
	return db
}

func getConnection() *sql.DB {
	if db == nil || db.Ping() != nil {
		connect()
	}
	return db
}
