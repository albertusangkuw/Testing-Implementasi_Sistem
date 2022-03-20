package handler

import (
	"encoding/json"
	"net/http"

	"github.com/Testing-Implementasi_Sistem/controller"
	model "github.com/Testing-Implementasi_Sistem/model"
	"github.com/gorilla/mux"
)

//Get MetaData Song
func GetSong(w http.ResponseWriter, r *http.Request) {
	var response model.MusicResponse

	errparse := r.ParseForm()
	if errparse != nil {
		controller.ResponseManager(&response.Response, 500, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	filterBY := ""
	var valuesList []interface{}

	vars := mux.Vars(r)
	IDmusic := vars["IDmusic"]
	searhing := false

	if len(IDmusic) == 0 {
		//Searching Features
		parseTitle := r.URL.Query()["title"]
		if parseTitle != nil {
			filterBY = " title LIKE   '%" + parseTitle[0] + "%'"
			searhing = true
		} else {
			//Searching Features
			filterBY, valuesList = controller.GenerateSQLWhere(r, []string{"idsong", "idalbum", "genre"}, "OR", "GET")
		}
	} else {
		// Select one id
		filterBY = " idsong=?"
		valuesList = append(valuesList, IDmusic)
	}

	// Convert data result set of user to data type user
	musics, err := controller.GetSong(searhing, filterBY, valuesList...)

	// Convert data result set of user to data type
	if len(musics) > 0 {
		response.Data = musics
		controller.ResponseManager(&response.Response, 200, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)

	} else if err.Error() == "500" {
		controller.ResponseManager(&response.Response, 500, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	} else {
		controller.ResponseManager(&response.Response, 404, "Data Not Found")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}

func LikeSong(w http.ResponseWriter, r *http.Request) {
	var response model.Response

	err := r.ParseForm()
	if err != nil {
		controller.ResponseManager(&response, 400, "Failed Insert Liked Song "+err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	vars := mux.Vars(r)
	IDmusic := vars["IDmusic"]
	anotherID := vars["anotherID"]

	errQuery := controller.LikeSong(IDmusic, anotherID)
	if errQuery == nil {
		controller.ResponseManager(&response, 200, "Success Insert Liked Song")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else if errQuery.Error() == "500" {
		controller.ResponseManager(&response, 500, errQuery.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else {
		controller.ResponseManager(&response, 400, "Failed Insert Liked Song ")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}
