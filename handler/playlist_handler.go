package handler

import (
	"encoding/json"
	"net/http"

	"github.com/Testing-Implementasi_Sistem/controller"
	model "github.com/Testing-Implementasi_Sistem/model"
	"github.com/gorilla/mux"
)

func AddSongPlaylist(w http.ResponseWriter, r *http.Request) {
	var response model.Response
	err := r.ParseForm()
	if err != nil {
		controller.ResponseManager(&response, 400, "Failed Insert Song to Playlist"+err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}
	vars := mux.Vars(r)
	IDplaylist := vars["IDplaylist"]
	IDmusic := vars["IDmusic"]

	if len(IDplaylist) > 0 && len(IDmusic) > 0 {
		errQuery := controller.AddSongPlaylist(IDplaylist, IDmusic)
		if errQuery == nil {
			controller.ResponseManager(&response, 200, "Success Insert Song to Playlist")
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)

		} else {
			controller.ResponseManager(&response, 500, errQuery.Error())
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)
		}
	} else {
		controller.ResponseManager(&response, 400, "Failed Insert Song to Playlist")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}

//Followed Playlist
func FollowedPlaylist(w http.ResponseWriter, r *http.Request) {
	var response model.Response

	err := r.ParseForm()
	if err != nil {
		controller.ResponseManager(&response, 400, "Failed Insert Following Playlist"+err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	vars := mux.Vars(r)
	IDplaylist := vars["IDplaylist"]
	IDuser := vars["IDuser"]

	errQuery := controller.FollowedPlaylist(IDplaylist, IDuser)
	if errQuery == nil {
		controller.ResponseManager(&response, 200, "Success Insert Following Playlist")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else if errQuery.Error() == "500" {
		controller.ResponseManager(&response, 500, errQuery.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else {
		controller.ResponseManager(&response, 400, "Failed Insert Following Playlist")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}

//UpdateUser is update data user like name, age, and address by id user
func UpdatePlaylist(w http.ResponseWriter, r *http.Request) {
	var response model.Response

	err := r.ParseForm()
	if err != nil {
		controller.ResponseManager(&response, 400, "Cannot parse request")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	vars := mux.Vars(r)
	IDplaylist := vars["IDplaylist"]

	updatedList := ""
	var valuesList []interface{}
	updatedList, valuesList = controller.GenerateSQLWhere(r, []string{"iduser", "nameplaylist", "urlimagecover"}, ",", "POST")
	valuesList = append(valuesList, IDplaylist)

	errQuery := controller.UpdatePlaylist(updatedList, valuesList)
	if errQuery == nil {
		controller.ResponseManager(&response, 200, "Success Update")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else if errQuery != nil {
		controller.ResponseManager(&response, 500, errQuery.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else {
		controller.ResponseManager(&response, 400, "No Row was Updated")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}
