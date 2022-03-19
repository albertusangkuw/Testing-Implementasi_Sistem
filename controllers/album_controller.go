package controllers

import (
	"database/sql"
	"encoding/json"
	"log"
	"net/http"
	"strconv"

	model "github.com/Testing-Implementasi_Sistem/models"
	"github.com/gorilla/mux"
)

func GetAllAlbum(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	query := "SELECT * FROM album"
	errparse := r.ParseForm()
	if errparse != nil {
		return
	}
	filterBY := ""
	var valuesList []interface{}

	vars := mux.Vars(r)
	IDalbum := vars["IDalbum"]
	searching := false
	if len(IDalbum) == 0 {
		//Searching Features
		parseTitle := r.URL.Query()["namealbum"]
		if parseTitle != nil {
			filterBY = " namealbum LIKE   '%" + parseTitle[0] + "%'"
			searching = true
		} else {
			filterBY, valuesList = GenerateSQLWhere(r, []string{"daterelease", "iduser", "genre"}, "OR", "GET")
		}
	} else {
		// Select one id
		filterBY = " idalbum=?"
		valuesList = append(valuesList, IDalbum)
	}
	if filterBY != "" {
		query += " WHERE " + filterBY
	}

	println(query)

	var rows *sql.Rows
	var err error
	if searching {
		rows, err = db.Query(query)
	} else {
		rows, err = db.Query(query, valuesList...)
	}

	var response model.AlbumResponse
	if err != nil {
		ResponseManager(&response.Response, 500, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	// Convert data result set of album to data type album
	var album model.Album
	var albums []model.Album
	for rows.Next() {
		if err := rows.Scan(&album.IDalbum, &album.DateRelease, &album.NameAlbum, &album.UrlImageCover, &album.Genre, &album.IDuser); err != nil {
			log.Print(err.Error())
		} else {
			//Select All Song
			listsongrs, err := db.Query("SELECT song.idsong ,song.title, song.idalbum, song.urlsongs, song.genre FROM song WHERE song.idalbum = ?", album.IDalbum)
			if err != nil {
				ResponseManager(&response.Response, 500, "")
				w.Header().Set("Content-Type", "application/json")
				json.NewEncoder(w).Encode(response)
				return
			}
			var song model.Music
			var listSongs []model.Music

			for listsongrs.Next() {
				if err := listsongrs.Scan(&song.IDsong, &song.Title, &song.IDalbum, &song.Urlsongs, &song.Genre); err != nil {
					log.Print(err.Error())
				}
				listSongs = append(listSongs, song)
			}
			album.ListSong = listSongs

			//Select All Followers
			var user model.User
			var listUsers []model.User
			listUserFollowingrs, err := db.Query("SELECT album_following.iduser, user.username , user.urlphotoprofile FROM album_following INNER JOIN user ON user.iduser = album_following.iduser WHERE album_following.idalbum=?", album.IDalbum)
			if err != nil {
				ResponseManager(&response.Response, 500, "")
				w.Header().Set("Content-Type", "application/json")
				json.NewEncoder(w).Encode(response)
				return
			}

			for listUserFollowingrs.Next() {
				if err := listUserFollowingrs.Scan(&user.IDuser, &user.Username, &user.UrlPhotoProfile); err != nil {
					log.Print(err.Error())
				}
				listUsers = append(listUsers, user)
			}
			album.IDFollowing = listUsers
			albums = append(albums, album)
		}
	}

	// Convert data result set of user to data type
	if len(albums) > 0 {
		response.Data = albums
		ResponseManager(&response.Response, 200, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)

	} else {
		ResponseManager(&response.Response, 404, "Data Not Found")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}

}

//Get Photo Profile
func GetPhotoAlbum(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	query := "SELECT iduser,urlimagecover FROM album WHERE idalbum=?"
	err := r.ParseForm()
	if err != nil {
		return
	}

	vars := mux.Vars(r)
	albumID := vars["IDalbum"]

	println(query)

	rows, err := db.Query(query, albumID)

	var response model.Response
	if err != nil {
		ResponseManager(&response, 500, err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	// Convert data result set of album to data type album
	var album model.Album
	album.IDuser = ""
	for rows.Next() {
		if err := rows.Scan(&album.IDuser, &album.UrlImageCover); err != nil {
			log.Print(err.Error())
		}
	}

	if album.IDuser != "" {
		println("Photo Profile :" + album.IDuser + " is requested")
		w.Header().Set("Content-Disposition", "inline; filename="+strconv.Quote(album.IDuser+".jpg"))
		w.Header().Set("Content-Type", "image/png")
		http.ServeFile(w, r, album.UrlImageCover)
	} else {
		ResponseManager(&response, 404, "Data Not Found")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}
func LikedAlbum(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()
	var response model.Response

	err := r.ParseForm()
	if err != nil {
		ResponseManager(&response, 400, "Failed Insert Liked Album"+err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	vars := mux.Vars(r)
	IDalbum := vars["IDalbum"]
	IDuser := vars["IDuser"]

	if len(IDalbum) > 0 && len(IDuser) > 0 {
		_, errQuery := db.Exec("INSERT INTO album_following(idalbum,iduser) VALUES(?,?)",
			IDalbum, IDuser,
		)
		if errQuery == nil {
			ResponseManager(&response, 200, "Success Insert Liked Album")
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)

		} else {
			ResponseManager(&response, 500, errQuery.Error())
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)
		}
	} else {
		ResponseManager(&response, 400, "Failed Insert Liked Album")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}

}

func UnLikedAlbum(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()
	var response model.Response

	err := r.ParseForm()
	if err != nil {
		ResponseManager(&response, 400, "Failed Delete Liked Album "+err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}
	vars := mux.Vars(r)

	IDalbum := vars["IDalbum"]
	IDuser := vars["IDuser"]

	if len(IDalbum) > 0 && len(IDuser) > 0 {
		res, errQuery := db.Exec("DELETE FROM album_following WHERE idalbum=? AND iduser=?",
			IDalbum, IDuser,
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
		ResponseManager(&response, 400, "Failed Delete Liked Album")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}

}
