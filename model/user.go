package model

import time "time"

type User struct {
	IDuser          string    `form:"iduser" json:"iduser"`
	Username        string    `form:"username" json:"username"`
	Email           string    `form:"email" json:"email"`
	Country         string    `form:"country" json:"country"`
	UrlPhotoProfile string    `form:"urlphotoprofile" json:"urlphotoprofile"`
	DateJoin        time.Time `form:"datejoin" json:"datejoin"`
	Categories      int       `form:"categories" json:"categories"`
}

type Artist struct {
	User
	Bio string `form:"bio" json:"bio"`
}

type RegularUser struct {
	User
	Gender      string    `form:"gender" json:"gender"`
	DateOfBirth time.Time `form:"dateofbirth" json:"dateofbirth"`
}

type UserResponse struct {
	Response
	DataArtis   []Artist      `form:"artist" json:"artist"`
	DataRegular []RegularUser `form:"regularuser" json:"regularuser"`
}

type UserResponseDetail struct {
	Response
	ID                   string   `form:"id" json:"id"`
	DataFollowers        []string `form:"datafollowers" json:"datafollowers"`
	DataFollowingRegular []string `form:"datafollowingregular" json:"datafollowingregular"`
	DataFollowingArtis   []string `form:"datafollowingartis" json:"datafollowingartis"`
	DataLikedSong        []string `form:"datalikedsong" json:"datalikedsong"`
	DataPlaylistOwned    []string `form:"dataplaylistowned" json:"dataplaylistowned"`
	DataPlaylistLiked    []string `form:"dataplaylistliked" json:"dataplaylistliked"`
	DataAlbum            []string `form:"dataalbum" json:"dataalbum"`
}
