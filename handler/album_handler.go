package handler

import (
	"encoding/json"
	"net/http"

	"github.com/Testing-Implementasi_Sistem/controller"
	model "github.com/Testing-Implementasi_Sistem/model"
	"github.com/gorilla/mux"
)

func LikedAlbum(w http.ResponseWriter, r *http.Request) {
	var response model.Response

	err := r.ParseForm()
	if err != nil {
		controller.ResponseManager(&response, 400, "Failed Insert Liked Album"+err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	vars := mux.Vars(r)
	IDalbum := vars["IDalbum"]
	IDuser := vars["IDuser"]

	errQuery := controller.LikedAlbum(IDalbum, IDuser)
	if errQuery == nil {
		controller.ResponseManager(&response, 200, "Success Insert Liked Album")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)

	} else if errQuery.Error() == "500" {
		controller.ResponseManager(&response, 500, errQuery.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else {
		controller.ResponseManager(&response, 400, "Failed Insert Liked Album")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}
