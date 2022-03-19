package controllers

import (
	"crypto/sha256"
	"encoding/hex"
	"encoding/json"
	"log"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"

	model "github.com/Testing-Implementasi_Sistem/model"
)

//UserLogin untuk melakukan pengecekan pada password dan email yang dimasukan
func UserLogin(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	query := "SELECT iduser,username FROM user WHERE email=? AND password=? "

	email := r.URL.Query()["email"]
	password := r.URL.Query()["password"]

	var response model.Response
	if len(email) == 0 || len(password) == 0 {
		ResponseManager(&response, 400, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}
	sha := sha256.New()
	sha.Write([]byte(password[0]))
	sha_password := hex.EncodeToString(sha.Sum(nil))

	rows, err := db.Query(query, email[0], sha_password)

	if err != nil {
		ResponseManager(&response, 500, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	//Check user login by name
	id := ""
	name := ""

	for rows.Next() {
		if err := rows.Scan(&id, &name); err != nil {
			log.Print(err.Error())
		}
	}

	if len(name) > 0 {
		generateToken(w, id, name, 0)
		ResponseManager(&response, 200, "Success Login")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else {
		ResponseManager(&response, 404, "Login Failed")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(&response)
	}
}
func ResetPassword(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	query := "SELECT iduser,username FROM user WHERE email=? "

	email := r.URL.Query()["email"]

	var response model.Response
	if len(email) == 0 {
		ResponseManager(&response, 400, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	rows, err := db.Query(query, email[0])

	if err != nil {
		ResponseManager(&response, 500, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	//Check user login by name
	id := ""
	name := ""

	for rows.Next() {
		if err := rows.Scan(&id, &name); err != nil {
			log.Print(err.Error())
		}
	}
	if len(name) > 0 {
		generateToken(w, id, name, 0)
		ResponseManager(&response, 200, "Success Login")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else {
		ResponseManager(&response, 404, "Login Failed")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(&response)
	}
}

func Logout(w http.ResponseWriter, r *http.Request) {
	resetUserToken(w)
	var response model.Response
	ResponseManager(&response, 200, "Success Logout")
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

func CheckCookie(w http.ResponseWriter, r *http.Request) {
	var response model.Response
	ResponseManager(&response, 200, "Cookie Valid")
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

//GetAllUsers or get user by some criteria
func GetAllUsers(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	//query := "SELECT iduser,username,email,country,urlphotoprofile,datejoin,categories FROM user"
	query := "SELECT iduser,email,username,urlphotoprofile,datejoin,categories FROM user"
	err := r.ParseForm()
	if err != nil {
		return
	}

	vars := mux.Vars(r)
	userID := vars["userID"]

	if len(userID) == 0 {
		//Searching Features
		parseUsername := r.URL.Query()["username"]
		parseId := r.URL.Query()["userId"]
		if parseUsername != nil {
			query += " WHERE " + " username LIKE   '%" + parseUsername[0] + "%'"
		} else if parseId != nil {
			query += " WHERE " + " iduser ='" + parseId[0] + "'"
		}
	} else {
		// Select one id
		query += " WHERE " + " email='" + userID + "'"
	}

	println(query)

	rows, err := db.Query(query)

	var response model.UserResponse
	if err != nil {
		ResponseManager(&response.Response, 500, err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	// Convert data result set of user to data type user
	var user model.User
	var reguler []model.RegularUser
	var artist []model.Artist
	for rows.Next() {
		if err := rows.Scan(&user.IDuser, &user.Email, &user.Username, &user.UrlPhotoProfile, &user.DateJoin, &user.Categories); err != nil {
			log.Print(err.Error())
		} else {
			if user.Categories == 1 {
				artistrs, err := db.Query("SELECT bio FROM artist WHERE iduser=?", user.IDuser)
				if err != nil {
					ResponseManager(&response.Response, 500, err.Error())
					w.Header().Set("Content-Type", "application/json")
					json.NewEncoder(w).Encode(response)
					return
				}
				var artistData model.Artist
				artistData.Bio = ""
				for artistrs.Next() {
					if err := artistrs.Scan(&artistData.Bio); err != nil {
						//log.Print(err.Error())
					}
					artistData.User = user
					artist = append(artist, artistData)
				}
			} else {
				regulerrs, err := db.Query("SELECT dateofbirth,gender FROM regular_user WHERE iduser=?", user.IDuser)
				if err != nil {
					ResponseManager(&response.Response, 500, err.Error())
					w.Header().Set("Content-Type", "application/json")
					json.NewEncoder(w).Encode(response)
					return
				}
				var regulerData model.RegularUser
				regulerData.Gender = ""
				for regulerrs.Next() {
					if err := regulerrs.Scan(&regulerData.DateOfBirth, &regulerData.Gender); err != nil {
						//log.Print(err.Error())
					}
					regulerData.User = user
					reguler = append(reguler, regulerData)
				}
			}
		}
	}

	// Convert data result set of user to data type
	if len(reguler) > 0 || len(artist) > 0 {
		response.DataArtis = artist
		response.DataRegular = reguler
		ResponseManager(&response.Response, 200, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)

	} else {
		ResponseManager(&response.Response, 404, "Data Not Found")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}

func GetDetailUser(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	//query := "SELECT iduser,username,email,country,urlphotoprofile,datejoin,categories FROM user"

	err := r.ParseForm()
	if err != nil {
		return
	}

	queryFollowing := "SELECT followinguserid, user.categories  FROM user_following INNER JOIN user ON user.iduser = user_following.followinguserid  WHERE userid=?"
	queryFollowers := "SELECT userid FROM user_following WHERE followinguserid=?"
	queryAlbum := "SELECT idalbum FROM album_following WHERE iduser=?"
	queryPlaylistOwned := "SELECT idplaylist FROM playlist WHERE iduser=?"
	queryPlaylistLiked := "SELECT idplaylist FROM playlist_following WHERE iduser=?"
	queryLikedSong := "SELECT idsong FROM song_like WHERE iduser=?"

	vars := mux.Vars(r)
	userID := vars["userID"]

	rowsFollowing, err := db.Query(queryFollowing, userID)

	var response model.UserResponseDetail
	if err != nil {
		ResponseManager(&response.Response, 500, err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	rowsFollowers, _ := db.Query(queryFollowers, userID)
	rowsAlbum, _ := db.Query(queryAlbum, userID)
	rowsPlaylistOwned, _ := db.Query(queryPlaylistOwned, userID)
	rowsPlaylistLiked, _ := db.Query(queryPlaylistLiked, userID)
	rowsLikedSong, _ := db.Query(queryLikedSong, userID)

	// Convert data result set of user to data type user

	var DataFollowingArtis []string
	var DataFollowingRegular []string
	var DataFollowers []string
	var DataPlaylistOwned []string
	var DataPlaylistLiked []string
	var DataAlbum []string
	var DataLikedSong []string

	for rowsFollowing.Next() {
		var following string
		var categories int
		if err := rowsFollowing.Scan(&following, &categories); err != nil {
			//log.Print(err.Error())
		} else {
			if categories == 1 {
				DataFollowingArtis = append(DataFollowingArtis, following)
			} else {
				DataFollowingRegular = append(DataFollowingRegular, following)
			}
		}
	}

	for rowsFollowers.Next() {
		var followers string
		if err := rowsFollowers.Scan(&followers); err != nil {
			//log.Print(err.Error())
		} else {
			DataFollowers = append(DataFollowers, followers)
		}
	}

	for rowsAlbum.Next() {
		var album string
		if err := rowsAlbum.Scan(&album); err != nil {
			//log.Print(err.Error())
		} else {
			DataAlbum = append(DataAlbum, album)
		}
	}

	for rowsPlaylistOwned.Next() {
		var playlist string
		if err := rowsPlaylistOwned.Scan(&playlist); err != nil {
			//log.Print(err.Error())
		} else {
			DataPlaylistOwned = append(DataPlaylistOwned, playlist)
		}
	}

	for rowsPlaylistLiked.Next() {
		var playlist string
		if err := rowsPlaylistLiked.Scan(&playlist); err != nil {
			//log.Print(err.Error())
		} else {
			DataPlaylistLiked = append(DataPlaylistLiked, playlist)
		}
	}

	for rowsLikedSong.Next() {
		var idsong string
		if err := rowsLikedSong.Scan(&idsong); err != nil {
			//log.Print(err.Error())
		} else {
			DataLikedSong = append(DataLikedSong, idsong)
		}
	}
	response.ID = userID
	response.DataAlbum = DataAlbum
	response.DataFollowers = DataFollowers
	response.DataFollowingArtis = DataFollowingArtis
	response.DataFollowingRegular = DataFollowingRegular
	response.DataPlaylistLiked = DataPlaylistLiked
	response.DataPlaylistOwned = DataPlaylistOwned
	response.DataLikedSong = DataLikedSong

	ResponseManager(&response.Response, 200, "")
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)

}

//Get Photo Profile
func GetPhotoProfile(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	query := "SELECT iduser,urlphotoprofile FROM user WHERE iduser=?"
	err := r.ParseForm()
	if err != nil {
		return
	}

	vars := mux.Vars(r)
	userID := vars["userID"]

	println(query)

	rows, err := db.Query(query, userID)

	var response model.Response
	if err != nil {
		ResponseManager(&response, 500, err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	// Convert data result set of user to data type user
	var user model.User
	user.IDuser = ""
	for rows.Next() {
		if err := rows.Scan(&user.IDuser, &user.UrlPhotoProfile); err != nil {
			log.Print(err.Error())
		}
	}

	if user.IDuser != "" && user.UrlPhotoProfile != "" {
		println("Photo Profile :" + user.IDuser + " is requested")
		w.Header().Set("Content-Disposition", "inline; filename="+strconv.Quote(user.IDuser+".jpg"))
		w.Header().Set("Content-Type", "image/png")
		http.ServeFile(w, r, user.UrlPhotoProfile)
	} else {
		ResponseManager(&response, 404, "Data Not Found")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}

//Register Regular User to database
func RegisterRegularUser(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	err := r.ParseForm()
	if err != nil {
		return
	}
	username := r.Form.Get("username")
	email := r.Form.Get("email")
	password := r.Form.Get("password")
	country := r.Form.Get("country")
	urlphotoprofile := r.Form.Get("urlphotoprofile")
	dateJoin := r.Form.Get("dateJoin")
	categories := 2

	var response model.UserResponse
	if len(username) > 0 && len(email) > 0 && len(password) > 0 && len(dateJoin) > 0 {

		newIdUser := GetMD5Hash(username + email + GetMD5Hash(password))
		sha := sha256.New()
		sha.Write([]byte(password))
		password = hex.EncodeToString(sha.Sum(nil))
		_, errQuery := db.Exec("INSERT INTO user VALUES (?,?,?,?,?,?,?,?)",
			newIdUser, username, email, password, country, urlphotoprofile, dateJoin, categories,
		)
		if errQuery == nil {
			db.Exec("INSERT INTO regular_user(iduser) VALUES (?)", newIdUser)
			ResponseManager(&response.Response, 200, "")
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)

		} else {
			ResponseManager(&response.Response, 500, errQuery.Error())
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)
		}
	} else {
		ResponseManager(&response.Response, 400, "Insert Failed ")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}

//DeleteUser is delete user by id user
func DeleteUser(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	err := r.ParseForm()
	if err != nil {
		return
	}
	vars := mux.Vars(r)
	userID := vars["userID"]

	// Asumsi Data hanya ada di table users dan regular_user
	res, errQuery := db.Exec("DELETE FROM regular_user WHERE iduser=?",
		userID,
	)
	var response model.UserResponse
	if errQuery != nil {
		ResponseManager(&response.Response, 500, "")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else {
		resUser, _ := db.Exec("DELETE FROM users WHERE iduser=?",
			userID,
		)
		nums, _ := res.RowsAffected()
		nums2, _ := resUser.RowsAffected()
		if nums > 0 && nums2 > 0 {
			ResponseManager(&response.Response, 200, "Delete Success")
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)
		} else {
			ResponseManager(&response.Response, 400, "No Row was Deleted")
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)
		}
	}
}

//UpdateUser is update data user like name, age, and address by id user
func UpdateUser(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	err := r.ParseForm()
	if err != nil {
		return

	}
	vars := mux.Vars(r)
	id := vars["IDuser"]

	var response model.Response
	var status = false

	// Using Variadic Function to pass
	updatedList := ""
	var valuesList []interface{}
	updatedList, valuesList = GenerateSQLWhere(r, []string{"username", "email", "password", "country", "urlphotoprofile"}, ",", "POST")
	valuesList = append(valuesList, id)

	if updatedList != "" {
		res, errQuery := db.Exec("UPDATE user SET "+updatedList+" WHERE iduser=?",
			valuesList...,
		)
		println("Hey ini " + id)
		if errQuery != nil {
			ResponseManager(&response, 500, errQuery.Error())
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)
		} else {
			nums, _ := res.RowsAffected()
			if nums > 0 {
				status = true
			}
		}
	}

	updatedList2, valuesList2 := GenerateSQLWhere(r, []string{"dateofbirth", "gender"}, ",", "POST")
	valuesList2 = append(valuesList2, id)
	if updatedList2 != "" {
		res, errQuery := db.Exec("UPDATE regular_user SET "+updatedList2+" WHERE iduser=?",
			valuesList2...,
		)
		if errQuery != nil {
			ResponseManager(&response, 500, errQuery.Error())
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)
		} else {
			nums, _ := res.RowsAffected()
			if nums > 0 {
				status = true
			}
		}
	}

	if status {
		ResponseManager(&response, 200, "Success Update")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else {
		ResponseManager(&response, 400, "No Row was Updated")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}

}

func FollowedUser(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()
	var response model.Response

	err := r.ParseForm()
	if err != nil {
		ResponseManager(&response, 400, "Failed Insert Following User "+err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	vars := mux.Vars(r)
	userID := vars["userID"]
	anotherID := vars["anotherID"]

	if len(userID) > 0 && len(anotherID) > 0 {
		_, errQuery := db.Exec("INSERT INTO user_following(userid,followinguserid) VALUES(?,?)",
			userID, anotherID,
		)
		if errQuery == nil {
			ResponseManager(&response, 200, "Success Insert Following User")
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)

		} else {
			ResponseManager(&response, 500, errQuery.Error())
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)
		}
	} else {
		ResponseManager(&response, 400, "Failed Insert Following User ")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}

}

func UnFollowedUser(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()
	var response model.Response

	err := r.ParseForm()
	if err != nil {
		ResponseManager(&response, 400, "Failed Delete Following User "+err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}
	vars := mux.Vars(r)
	userID := vars["userID"]
	anotherID := vars["anotherID"]

	if len(userID) > 0 && len(anotherID) > 0 {
		res, errQuery := db.Exec("DELETE FROM user_following WHERE userid=? AND followinguserid=?",
			userID, anotherID,
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
		ResponseManager(&response, 400, "Failed Delete Following User ")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}

}

func GetHistoryUser(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()

	err := r.ParseForm()
	if err != nil {
		return
	}

	queryUserHistory := "SELECT id_list,type,date FROM user_history WHERE id_user=? ORDER BY date DESC"

	vars := mux.Vars(r)
	userID := vars["userID"]

	rowsHistory, err := db.Query(queryUserHistory, userID)

	var response model.RecentlyResponse

	if err != nil {
		ResponseManager(&response.Response, 500, err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	// Convert data result set of user to data type user

	var DataRecently []model.Recently
	var data model.Recently
	counter := 0
	for rowsHistory.Next() {
		if counter > 9 {
			break
		}
		if err := rowsHistory.Scan(&data.IDlist, &data.Type, &data.Date); err != nil {
			//log.Print(err.Error())
		} else {
			DataRecently = append(DataRecently, data)
		}
		counter++
	}

	response.Data = DataRecently
	ResponseManager(&response.Response, 200, "")
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)

}

func InsertHistoryUser(w http.ResponseWriter, r *http.Request) {
	db := connect()
	defer db.Close()
	var response model.Response

	err := r.ParseForm()
	if err != nil {
		ResponseManager(&response, 400, "Failed Insert History User "+err.Error())
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
		return
	}

	vars := mux.Vars(r)
	userID := vars["userID"]
	idlist := r.Form.Get("idlist")
	tipe := r.Form.Get("type")
	date := r.Form.Get("date")

	if len(userID) > 0 {
		_, errQuery := db.Exec("INSERT INTO user_history(id_user,id_list,type,date) VALUES(?,?,?,?)",
			userID, idlist, tipe, date,
		)
		if errQuery == nil {
			ResponseManager(&response, 200, "Success Insert History User")
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)

		} else {
			ResponseManager(&response, 500, errQuery.Error())
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)
		}
	} else {
		ResponseManager(&response, 400, "Failed Insert History User ")
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}

}
