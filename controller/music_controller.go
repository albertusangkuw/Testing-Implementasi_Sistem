package controller

import (
	"database/sql"
	"encoding/json"
	"errors"
	"log"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"

	model "github.com/Testing-Implementasi_Sistem/model"
)

//Get MetaData Song
func GetSong(searhing bool, filterBY string, valuesList ...interface{}) ([]model.Music, error) {
	db := connect()
	defer db.Close()

	query := "SELECT * FROM song"

	if filterBY != "" {
		query += " WHERE " + filterBY
	}

	println(query)
	var rows *sql.Rows
	var err error

	if searhing {
		rows, err = db.Query(query)
	} else {
		rows, err = db.Query(query, valuesList...)
	}

	if err != nil {
		return nil, errors.New("500")
	}

	// Convert data result set of user to data type user
	var music model.Music
	var musics []model.Music

	for rows.Next() {
		if err := rows.Scan(&music.IDsong, &music.IDalbum, &music.Title, &music.Urlsongs, &music.Genre); err != nil {
			log.Print(err.Error())
		} else {
			musics = append(musics, music)
		}
	}

	if len(musics) > 0 {
		return musics, nil
	}
	return nil, errors.New("404")
}

//Get Data Song
func GetSongFile(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	var response model.Response
	query := "SELECT idsong,urlsongs FROM song WHERE idsong=?"

	err := r.ParseForm()
	if err != nil {
		ResponseManager(&response, 500, err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	vars := mux.Vars(r)
	IDmusic := vars["IDmusic"]

	if len(IDmusic) == 0 {
		ResponseManager(&response, 500, "ID Music is Required")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	rows, err := db.Query(query, IDmusic)

	if err != nil {
		ResponseManager(&response, 500, err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	// Convert data result set of user to data type user
	var music model.Music

	for rows.Next() {
		if err := rows.Scan(&music.IDsong, &music.Urlsongs); err != nil {
			log.Print(err.Error())
		}
	}

	// Convert data result set of user to data type
	if music.Urlsongs != "" {
		println("Music :" + IDmusic + " is requested")
		w.Header().Set("Content-Disposition", "inline; filename="+strconv.Quote(IDmusic+".mp3"))
		w.Header().Set("Content-Type", "audio/mp3")
		http.ServeFile(w, r, music.Urlsongs)
	} else {
		ResponseManager(&response, 404, "Data Not Found")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}

func LikeSong(IDmusic string, anotherID string) error {
	db := connect()
	defer db.Close()

	if len(IDmusic) > 0 && len(anotherID) > 0 {
		_, errQuery := db.Exec("INSERT INTO song_like(iduser,idsong) VALUES(?,?)",
			anotherID, IDmusic,
		)
		if errQuery == nil {
			return nil
		} else {
			return errors.New("500")
		}
	}
	return errors.New("400")
}

func UnLikeSong(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()
	var response model.Response

	err := r.ParseForm()
	if err != nil {
		ResponseManager(&response, 400, "Failed Delete Liked Song "+err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}
	vars := mux.Vars(r)
	IDmusic := vars["IDmusic"]
	anotherID := vars["anotherID"]

	if len(IDmusic) > 0 && len(anotherID) > 0 {
		res, errQuery := db.Exec("DELETE FROM song_like WHERE iduser=? AND idsong=?",
			anotherID, IDmusic,
		)
		if errQuery != nil {
			ResponseManager(&response, 500, errQuery.Error())
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)

		} else {
			nums, _ := res.RowsAffected()
			if nums > 0 {
				ResponseManager(&response, 200, "Delete Success")
				w.Header().Set("Content-Type", "application/json")
				json.NewEncoder(w).Encode(response)
			} else {
				ResponseManager(&response, 404, "No Row was Deleted")
				w.Header().Set("Content-Type", "application/json")
				json.NewEncoder(w).Encode(response)
			}
		}
	} else {
		ResponseManager(&response, 400, "Failed Delete Liked Song ")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}
