package model

import time "time"

type Playlist struct {
	IDplaylist    uint32    `form:"idplaylist" json:"idplaylist"`
	IDuser        string    `form:"iduser" json:"iduser"`
	NamePlaylist  string    `form:"nameplaylist" json:"nameplaylist"`
	UrlImageCover string    `form:"urlimagecover" json:"urlimagecover"`
	DateCreated   time.Time `form:"datecreated" json:"datecreated"`
	ListSong      []Music   `form:"listsong" json:"listsong"`
	IDFollowing   []User    `form:"userfollowing" json:"userfollowing"`
}
type PlaylistResponse struct {
	Response
	Data []Playlist `form:"data" json:"data"`
}

type Recently struct {
	IDlist int       `form:"idlist" json:"idlist"`
	Type   int       `form:"type" json:"type"`
	Date   time.Time `form:"date" json:"date"`
}
type RecentlyResponse struct {
	Response
	Data []Recently `form:"data" json:"data"`
}
