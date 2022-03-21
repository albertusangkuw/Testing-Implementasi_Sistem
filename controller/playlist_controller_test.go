package controller

import "testing"

func TestAddSongPlaylist(t *testing.T) {
	idPlaylist := "1"
	idMusic := "1"
	result := AddSongPlaylist(idPlaylist, idMusic)
	if result != nil {
		t.Error("Add Song to Playlist Gagal : " + result.Error())
	}
}

func TestFollowedPlaylist(t *testing.T) {
	idPlaylist := "1"
	idUser := "7c20f5ef71c38abb1d0c1f1b6eb4459f"
	result := FollowedPlaylist(idPlaylist, idUser)
	if result != nil {
		t.Error("Followed Playlist Gagal : " + result.Error())
	}
}

func TestUpdatePlaylist(t *testing.T) {
	updatedList := "NamePlaylist=?,UrlImageCover=?"
	var valuesList []interface{}
	valuesList = append(valuesList, "Fun Playlist")
	valuesList = append(valuesList, "https://image.shutterstock.com/723500997.jpg")
	valuesList = append(valuesList, 1)
	result := UpdatePlaylist(updatedList, valuesList)
	if result != nil {
		t.Error("Update Playlist Gagal : " + result.Error())
	}
}
