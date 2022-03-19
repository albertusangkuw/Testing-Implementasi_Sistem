package model

type Music struct {
	IDsong   uint32 `form:"idsong" json:"idsong"`
	IDalbum  uint32 `form:"idalbum" json:"idalbum"`
	Title    string `form:"title" json:"title"`
	Urlsongs string `form:"urlsongs" json:"urlsongs"`
	Genre    string `form:"genre" json:"genre"`
}
type MusicResponse struct {
	Response
	Data []Music `form:"data" json:"data"`
}
