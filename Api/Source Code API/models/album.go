package models

import time "time"

type Album struct {
	IDalbum       uint32    `form:"idalbum" json:"idalbum"`
	IDuser        string    `form:"iduser" json:"iduser"`
	NameAlbum     string    `form:"namealbum" json:"namealbum"`
	UrlImageCover string    `form:"urlimagecover" json:"urlimagecover"`
	Genre         string    `form:"genre" json:"genre"`
	DateRelease   time.Time `form:"daterelease" json:"daterelease"`
	ListSong      []Music   `form:"listsong" json:"listsong"`
	IDFollowing   []User    `form:"userfollowing" json:"userfollowing"`
}
type AlbumResponse struct {
	Response
	Data []Album `form:"data" json:"data"`
}
