package controller

import (
	"encoding/json"
	"errors"
	"log"
	"net/http"

	"github.com/gorilla/mux"

	model "github.com/Testing-Implementasi_Sistem/model"
)

//GetAllUsers or get user by some criteria
func GetAllPlaylist(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	query := "SELECT * FROM playlist"
	err := r.ParseForm()
	if err != nil {
		return
	}
	filterBY := ""
	var valuesList []interface{}

	vars := mux.Vars(r)
	userID := vars["IDplaylist"]

	if len(userID) == 0 {
		//Searching Features
		filterBY, valuesList = GenerateSQLWhere(r, []string{"datecreated", "iduser", "nameplaylist"}, "OR", "GET")
	} else {
		// Select one id
		filterBY = " idplaylist=?"
		valuesList = append(valuesList, userID)
	}
	if filterBY != "" {
		query += " WHERE " + filterBY
	}

	println(query)

	rows, err := db.Query(query, valuesList...)

	var response model.PlaylistResponse
	if err != nil {
		ResponseManager(&response.Response, 500, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	// Convert data result set of playlist to data type playlist
	var playlist model.Playlist
	var playlists []model.Playlist
	for rows.Next() {
		if err := rows.Scan(&playlist.IDplaylist, &playlist.DateCreated, &playlist.IDuser, &playlist.NamePlaylist, &playlist.UrlImageCover); err != nil {
			log.Print(err.Error())
		} else {
			//Select All Song
			listsongrs, err := db.Query("SELECT playlist_song.idsong,song.title, song.idalbum, song.urlsongs, song.genre FROM playlist_song INNER JOIN song ON song.idsong = playlist_song.idsong WHERE playlist_song.idplaylist=?", playlist.IDplaylist)
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
			playlist.ListSong = listSongs

			//Select All Followers
			var user model.User
			var listUsers []model.User
			listUserFollowingrs, err := db.Query("SELECT playlist_following.iduser, user.username , user.urlphotoprofile FROM playlist_following INNER JOIN user ON user.iduser = playlist_following.iduser WHERE idplaylist=?", playlist.IDplaylist)
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
			playlist.IDFollowing = listUsers
			playlists = append(playlists, playlist)
		}
	}

	// Convert data result set of user to data type
	if len(playlists) > 0 {
		response.Data = playlists
		ResponseManager(&response.Response, 200, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)

	} else {
		ResponseManager(&response.Response, 404, "Data Not Found")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}

}

//Register Regular User to database
func AddPlaylist(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	err := r.ParseForm()
	if err != nil {
		return
	}

	iduser := r.Form.Get("iduser")
	namePlaylist := r.Form.Get("namePlaylist")
	urlImageCover := r.Form.Get("urlImageCover")
	dateCreated := r.Form.Get("dateCreated")

	var response model.Response
	if len(iduser) > 0 && len(namePlaylist) > 0 && len(dateCreated) > 0 {
		_, errQuery := db.Exec("INSERT INTO playlist(iduser,nameplaylist,urlimagecover,datecreated) VALUES (?,?,?,?)",
			iduser, namePlaylist, urlImageCover, dateCreated,
		)
		if errQuery == nil {
			ResponseManager(&response, 200, "")
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)

		} else {
			ResponseManager(&response, 500, errQuery.Error())
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)
		}
	} else {
		ResponseManager(&response, 400, "Insert Failed ")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}

//DeleteUser is delete user by id user
func DeletePlaylist(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	err := r.ParseForm()
	if err != nil {
		return
	}

	vars := mux.Vars(r)
	IDplaylist := vars["IDplaylist"]
	println("Test  -L>" + IDplaylist)
	res, errQuery := db.Exec("DELETE FROM playlist_song WHERE idplaylist=?",
		IDplaylist,
	)
	var response model.Response
	if errQuery != nil {
		ResponseManager(&response, 500, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else {
		resUser, _ := db.Exec("DELETE FROM playlist WHERE idplaylist=?",
			IDplaylist,
		)
		nums, _ := res.RowsAffected()
		nums2, _ := resUser.RowsAffected()
		if nums > 0 || nums2 > 0 {
			ResponseManager(&response, 200, "Delete Success")
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)
		} else {
			ResponseManager(&response, 400, "No Row was Deleted")
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)
		}
	}
}

//UpdateUser is update data user like name, age, and address by id user
func UpdatePlaylist(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	err := r.ParseForm()
	if err != nil {
		return
	}

	vars := mux.Vars(r)
	IDplaylist := vars["IDplaylist"]
	println("Test : " + IDplaylist)
	var response model.Response

	// Using Variadic Function to pass
	updatedList := ""
	var valuesList []interface{}
	updatedList, valuesList = GenerateSQLWhere(r, []string{"iduser", "nameplaylist", "urlimagecover"}, ",", "POST")
	valuesList = append(valuesList, IDplaylist)

	if updatedList != "" {
		res, errQuery := db.Exec("UPDATE playlist SET "+updatedList+" WHERE idplaylist=?",
			valuesList...,
		)
		if errQuery != nil {
			ResponseManager(&response, 500, errQuery.Error())
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)
		} else {
			nums, _ := res.RowsAffected()
			if nums > 0 {
				ResponseManager(&response, 200, "Success Update")
				w.Header().Set("Content-Type", "application/json")
				json.NewEncoder(w).Encode(response)
			} else {
				ResponseManager(&response, 400, "No Row was Updated")
				w.Header().Set("Content-Type", "application/json")
				json.NewEncoder(w).Encode(response)
			}
		}
	}

}

func FollowedPlaylist(IDplaylist string, IDuser string) error {
	db := connect()
	defer db.Close()

	if len(IDplaylist) > 0 && len(IDuser) > 0 {
		_, errQuery := db.Exec("INSERT INTO playlist_following(idplaylist,iduser) VALUES(?,?)",
			IDplaylist, IDuser,
		)
		if errQuery == nil {
			return nil
		} else {
			return errors.New("500")
		}
	} else {
		return errors.New("400")
	}
}

func UnFollowedPlaylist(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()
	var response model.Response

	err := r.ParseForm()
	if err != nil {
		ResponseManager(&response, 400, "Failed Delete Following Playlist "+err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}
	vars := mux.Vars(r)

	IDplaylist := vars["IDplaylist"]
	IDuser := vars["IDuser"]

	if len(IDplaylist) > 0 && len(IDuser) > 0 {
		res, errQuery := db.Exec("DELETE FROM playlist_following WHERE idplaylist=? AND iduser=?",
			IDplaylist, IDuser,
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
		ResponseManager(&response, 400, "Failed Delete Following Playlist ")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}

}

func AddSongPlaylist(IDplaylist string, IDmusic string) error {
	db := connect()
	defer db.Close()

	_, errQuery := db.Exec("INSERT INTO playlist_song(idplaylist,idsong) VALUES(?,?)",
		IDplaylist, IDmusic,
	)
	if errQuery == nil {
		return nil
	} else {
		return errors.New("500")
	}
}

func RemoveSongPlaylist(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()
	var response model.Response

	err := r.ParseForm()
	if err != nil {
		ResponseManager(&response, 400, "Failed Delete Song to Playlist "+err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}
	vars := mux.Vars(r)

	IDplaylist := vars["IDplaylist"]
	IDmusic := vars["IDmusic"]

	if len(IDplaylist) > 0 && len(IDmusic) > 0 {
		res, errQuery := db.Exec("DELETE FROM playlist_song WHERE idplaylist=? AND idsong=?",
			IDplaylist, IDmusic,
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
		ResponseManager(&response, 400, "Failed Delete Song to Playlist ")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}

}
