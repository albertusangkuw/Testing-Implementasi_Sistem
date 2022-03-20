//Albertus Septian Angkuw 1119002
//Elangel Neilea Shaday 1119038
package main

import (
	"fmt"
	"log"
	"net/http"

	c "github.com/Testing-Implementasi_Sistem/controller"
	"github.com/Testing-Implementasi_Sistem/handler"
	_ "github.com/go-sql-driver/mysql"
	"github.com/gorilla/mux"
	"github.com/rs/cors"
)

func main() {
	router := mux.NewRouter()

	router.HandleFunc("/login", handler.UserLogin).Methods("GET")
	router.HandleFunc("/logout", handler.Logout).Methods("GET")
	router.HandleFunc("/checkcookie", handler.Authenticate(c.CheckCookie, 0)).Methods("GET")
	router.HandleFunc("/registrasi", handler.RegisterRegularUser).Methods("POST")
	router.HandleFunc("/resetpassword", handler.ResetPassword).Methods("GET")

	router.HandleFunc("/users", c.GetAllUsers).Methods("GET")
	router.HandleFunc("/users/{userID}", c.GetAllUsers).Methods("GET")
	router.HandleFunc("/users/{userID}/photo", c.GetPhotoProfile).Methods("GET")
	router.HandleFunc("/users/{userID}/detail", handler.Authenticate(c.GetDetailUser, 0)).Methods("GET")
	router.HandleFunc("/users/{IDuser}/update", handler.Authenticate(c.UpdateUser, 0)).Methods("PUT")
	router.HandleFunc("/users/{userID}/delete", handler.Authenticate(c.DeleteUser, 0)).Methods("DELETE")
	router.HandleFunc("/users/{userID}/history", handler.Authenticate(handler.GetHistoryUser, 0)).Methods("GET")
	router.HandleFunc("/users/{userID}/history", handler.Authenticate(handler.InsertHistoryUser, 0)).Methods("POST")

	router.HandleFunc("/users/{userID}/following/{anotherID}", handler.Authenticate(c.FollowedUser, 0)).Methods("POST")
	router.HandleFunc("/users/{userID}/following/{anotherID}/remove", handler.Authenticate(c.UnFollowedUser, 0)).Methods("DELETE")

	router.HandleFunc("/music", handler.Authenticate(handler.GetSong, 0)).Methods("GET")
	router.HandleFunc("/music/{IDmusic}", handler.Authenticate(handler.GetSong, 0)).Methods("GET")
	router.HandleFunc("/music/{IDmusic}/data", c.GetSongFile).Methods("GET")

	router.HandleFunc("/music/{IDmusic}/likes/{anotherID}", handler.Authenticate(handler.LikeSong, 0)).Methods("POST")
	router.HandleFunc("/music/{IDmusic}/likes/{anotherID}/remove", handler.Authenticate(c.UnLikeSong, 0)).Methods("DELETE")

	router.HandleFunc("/playlist", handler.Authenticate(c.AddPlaylist, 0)).Methods("POST")
	router.HandleFunc("/playlist", handler.Authenticate(c.GetAllPlaylist, 0)).Methods("GET")
	router.HandleFunc("/playlist/{IDplaylist}", handler.Authenticate(c.GetAllPlaylist, 0)).Methods("GET")
	router.HandleFunc("/playlist/{IDplaylist}/update", handler.Authenticate(c.UpdatePlaylist, 0)).Methods("PUT")
	router.HandleFunc("/playlist/{IDplaylist}/remove", handler.Authenticate(c.DeletePlaylist, 0)).Methods("DELETE")

	router.HandleFunc("/playlist/{IDplaylist}/music/{IDmusic}", handler.Authenticate(handler.AddSongPlaylist, 0)).Methods("POST")
	router.HandleFunc("/playlist/{IDplaylist}/music/{IDmusic}/remove", handler.Authenticate(c.RemoveSongPlaylist, 0)).Methods("DELETE")
	router.HandleFunc("/playlist/{IDplaylist}/following/{IDuser}", handler.Authenticate(c.FollowedPlaylist, 0)).Methods("POST")
	router.HandleFunc("/playlist/{IDplaylist}/following/{IDuser}/remove", handler.Authenticate(c.UnFollowedPlaylist, 0)).Methods("DELETE")

	router.HandleFunc("/album", handler.Authenticate(c.GetAllAlbum, 0)).Methods("GET")
	router.HandleFunc("/album/{IDalbum}", handler.Authenticate(c.GetAllAlbum, 0)).Methods("GET")
	router.HandleFunc("/album/{IDalbum}/photo", c.GetPhotoAlbum).Methods("GET")
	router.HandleFunc("/album/{IDalbum}/likes/{IDuser}", handler.Authenticate(c.LikedAlbum, 0)).Methods("POST")
	router.HandleFunc("/album/{IDalbum}/likes/{IDuser}/remove", handler.Authenticate(c.UnLikedAlbum, 0)).Methods("DELETE")

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
