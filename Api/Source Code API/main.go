//Albertus Septian Angkuw 1119002
//Elangel Neilea Shaday 1119038
package main

import (
	"fmt"
	"log"
	"net/http"

	"github.com/APIE-Music/controllers"
	c "github.com/APIE-Music/controllers"
	_ "github.com/go-sql-driver/mysql"
	"github.com/gorilla/mux"
	"github.com/rs/cors"
)

func main() {
	router := mux.NewRouter()

	router.HandleFunc("/login", c.UserLogin).Methods("GET")
	router.HandleFunc("/logout", c.Logout).Methods("GET")
	router.HandleFunc("/checkcookie", controllers.Authenticate(c.CheckCookie, 0)).Methods("GET")
	router.HandleFunc("/registrasi", c.RegisterRegularUser).Methods("POST")
	router.HandleFunc("/resetpassword", c.ResetPassword).Methods("GET")

	router.HandleFunc("/users", c.GetAllUsers).Methods("GET")
	router.HandleFunc("/users/{userID}", c.GetAllUsers).Methods("GET")
	router.HandleFunc("/users/{userID}/photo", c.GetPhotoProfile).Methods("GET")
	router.HandleFunc("/users/{userID}/detail", controllers.Authenticate(c.GetDetailUser, 0)).Methods("GET")
	router.HandleFunc("/users/{IDuser}/update", controllers.Authenticate(c.UpdateUser, 0)).Methods("PUT")
	router.HandleFunc("/users/{userID}/delete", controllers.Authenticate(c.DeleteUser, 0)).Methods("DELETE")
	router.HandleFunc("/users/{userID}/history", controllers.Authenticate(c.GetHistoryUser, 0)).Methods("GET")
	router.HandleFunc("/users/{userID}/history", controllers.Authenticate(c.InsertHistoryUser, 0)).Methods("POST")

	router.HandleFunc("/users/{userID}/following/{anotherID}", controllers.Authenticate(c.FollowedUser, 0)).Methods("POST")
	router.HandleFunc("/users/{userID}/following/{anotherID}/remove", controllers.Authenticate(c.UnFollowedUser, 0)).Methods("DELETE")

	router.HandleFunc("/music", controllers.Authenticate(c.GetSong, 0)).Methods("GET")
	router.HandleFunc("/music/{IDmusic}", controllers.Authenticate(c.GetSong, 0)).Methods("GET")
	router.HandleFunc("/music/{IDmusic}/data", c.GetSongFile).Methods("GET")

	router.HandleFunc("/music/{IDmusic}/likes/{anotherID}", controllers.Authenticate(c.LikeSong, 0)).Methods("POST")
	router.HandleFunc("/music/{IDmusic}/likes/{anotherID}/remove", controllers.Authenticate(c.UnLikeSong, 0)).Methods("DELETE")

	router.HandleFunc("/playlist", controllers.Authenticate(c.AddPlaylist, 0)).Methods("POST")
	router.HandleFunc("/playlist", controllers.Authenticate(c.GetAllPlaylist, 0)).Methods("GET")
	router.HandleFunc("/playlist/{IDplaylist}", controllers.Authenticate(c.GetAllPlaylist, 0)).Methods("GET")
	router.HandleFunc("/playlist/{IDplaylist}/update", controllers.Authenticate(c.UpdatePlaylist, 0)).Methods("PUT")
	router.HandleFunc("/playlist/{IDplaylist}/remove", controllers.Authenticate(c.DeletePlaylist, 0)).Methods("DELETE")

	router.HandleFunc("/playlist/{IDplaylist}/music/{IDmusic}", controllers.Authenticate(c.AddSongPlaylist, 0)).Methods("POST")
	router.HandleFunc("/playlist/{IDplaylist}/music/{IDmusic}/remove", controllers.Authenticate(c.RemoveSongPlaylist, 0)).Methods("DELETE")
	router.HandleFunc("/playlist/{IDplaylist}/following/{IDuser}", controllers.Authenticate(c.FollowedPlaylist, 0)).Methods("POST")
	router.HandleFunc("/playlist/{IDplaylist}/following/{IDuser}/remove", controllers.Authenticate(c.UnFollowedPlaylist, 0)).Methods("DELETE")

	router.HandleFunc("/album", controllers.Authenticate(c.GetAllAlbum, 0)).Methods("GET")
	router.HandleFunc("/album/{IDalbum}", controllers.Authenticate(c.GetAllAlbum, 0)).Methods("GET")
	router.HandleFunc("/album/{IDalbum}/photo", c.GetPhotoAlbum).Methods("GET")
	router.HandleFunc("/album/{IDalbum}/likes/{IDuser}", controllers.Authenticate(c.LikedAlbum, 0)).Methods("POST")
	router.HandleFunc("/album/{IDalbum}/likes/{IDuser}/remove", controllers.Authenticate(c.UnLikedAlbum, 0)).Methods("DELETE")

	corsHandler := cors.New(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT"},
		AllowCredentials: true,
		Debug:            true,
	})
	handler := corsHandler.Handler(router)
	http.Handle("/", handler)
	fmt.Println("Connected to port 8081")
	log.Fatal(http.ListenAndServe(":8081", router))
}
