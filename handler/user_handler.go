package handler

import (
	"encoding/json"
	"net/http"

	controller "github.com/Testing-Implementasi_Sistem/controller"
	model "github.com/Testing-Implementasi_Sistem/model"
	"github.com/gorilla/mux"
)

//UserLogin untuk melakukan pengecekan pada password dan email yang dimasukan
func UserLogin(w http.ResponseWriter, r *http.Request) {
	var response model.Response
	email := r.URL.Query()["email"]
	password := r.URL.Query()["password"]

	if len(email) == 0 || len(password) == 0 {
		controller.ResponseManager(&response, 400, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	id, name, err := controller.UserLogin(email[0], password[0])

	if len(name) > 0 {
		GenerateToken(w, id, name, 0)
		controller.ResponseManager(&response, 200, "Success Login")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else if err != nil && err.Error() == "500" {
		controller.ResponseManager(&response, 500, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else {
		controller.ResponseManager(&response, 404, "Login Failed")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(&response)
	}
}

func ResetPassword(w http.ResponseWriter, r *http.Request) {
	email := r.URL.Query()["email"]

	var response model.Response

	if len(email) == 0 {
		controller.ResponseManager(&response, 400, "Reset Failed")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(&response)
		return

	}
	id, name, err := controller.ResetPassword(email[0])

	//Check user login by name
	if len(name) > 0 {
		GenerateToken(w, id, name, 0)
		controller.ResponseManager(&response, 200, "Success Reset")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else if err.Error() == "500" {
		controller.ResponseManager(&response, 500, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	} else {
		controller.ResponseManager(&response, 400, "Reset Failed")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(&response)
	}
}

func Logout(w http.ResponseWriter, r *http.Request) {
	resetUserToken(w)
	var response model.Response
	controller.ResponseManager(&response, 200, "Success Logout")
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

//Register Regular User to database
func RegisterRegularUser(w http.ResponseWriter, r *http.Request) {
	var response model.UserResponse
	err := r.ParseForm()
	if err != nil {
		controller.ResponseManager(&response.Response, 400, "Insert Failed ")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}
	username := r.Form.Get("username")
	email := r.Form.Get("email")
	password := r.Form.Get("password")
	country := r.Form.Get("country")
	urlphotoprofile := r.Form.Get("urlphotoprofile")
	dateJoin := r.Form.Get("dateJoin")
	categories := 2

	errQuery := controller.RegisterRegularUser(username, email, password, country, urlphotoprofile, dateJoin, categories)
	if errQuery == nil {
		controller.ResponseManager(&response.Response, 200, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else if errQuery.Error() == "400" {
		controller.ResponseManager(&response.Response, 400, "Insert Failed ")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else {
		controller.ResponseManager(&response.Response, 500, errQuery.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}

func GetHistoryUser(w http.ResponseWriter, r *http.Request) {
	var response model.RecentlyResponse

	err := r.ParseForm()
	if err != nil {
		controller.ResponseManager(&response.Response, 400, err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	vars := mux.Vars(r)
	userID := vars["userID"]

	// Convert data result set of user to data type user
	DataRecently, err := controller.GetHistoryUser(userID)

	if err != nil {
		controller.ResponseManager(&response.Response, 500, err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}
	response.Data = DataRecently
	controller.ResponseManager(&response.Response, 200, "")
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

func InsertHistoryUser(w http.ResponseWriter, r *http.Request) {
	var response model.Response

	err := r.ParseForm()
	if err != nil {
		controller.ResponseManager(&response, 400, "Failed Insert History User "+err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	vars := mux.Vars(r)
	userID := vars["userID"]
	idlist := r.Form.Get("idlist")
	tipe := r.Form.Get("type")
	date := r.Form.Get("date")

	errQuery := controller.InsertHistoryUser(userID, idlist, tipe, date)
	if errQuery == nil {
		controller.ResponseManager(&response, 200, "Success Insert History User")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else if errQuery.Error() == "500" {
		controller.ResponseManager(&response, 500, errQuery.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else {
		controller.ResponseManager(&response, 400, "Failed Insert History User ")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}
