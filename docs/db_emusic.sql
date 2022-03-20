-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Mar 20, 2022 at 01:40 PM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_emusic`
--

-- --------------------------------------------------------

--
-- Table structure for table `album`
--

CREATE TABLE `album` (
  `IDalbum` int(11) NOT NULL,
  `DateRelease` datetime NOT NULL,
  `NameAlbum` varchar(64) NOT NULL,
  `UrlImageCover` varchar(64) NOT NULL,
  `Genre` varchar(64) NOT NULL,
  `IDuser` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `album`
--

INSERT INTO `album` (`IDalbum`, `DateRelease`, `NameAlbum`, `UrlImageCover`, `Genre`, `IDuser`) VALUES
(1, '2022-03-20 12:00:18', 'Lilac', 'https://e17b5d43518b7a8412449ca642127094.jpg', 'Love Song', '0ee6d02530649ef3c3c2a81da45f72a0'),
(2, '2022-03-20 12:00:18', 'Queen', 'https://a050bcfb6304cf0a1784968adca649d1.jpg', 'Love Song', '365aaaf8834f326e5d3925cc82693605');

-- --------------------------------------------------------

--
-- Table structure for table `album_following`
--

CREATE TABLE `album_following` (
  `idalbum` int(11) NOT NULL,
  `iduser` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `artist`
--

CREATE TABLE `artist` (
  `IDuser` varchar(64) NOT NULL,
  `Bio` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `artist`
--

INSERT INTO `artist` (`IDuser`, `Bio`) VALUES
('0ee6d02530649ef3c3c2a81da45f72a0', 'Solo K-POP singer'),
('365aaaf8834f326e5d3925cc82693605', 'Grup K-POP Singer');

-- --------------------------------------------------------

--
-- Table structure for table `playlist`
--

CREATE TABLE `playlist` (
  `IDplaylist` int(11) NOT NULL,
  `DateCreated` datetime NOT NULL,
  `IDuser` varchar(64) NOT NULL,
  `NamePlaylist` varchar(64) NOT NULL,
  `UrlImageCover` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `playlist`
--

INSERT INTO `playlist` (`IDplaylist`, `DateCreated`, `IDuser`, `NamePlaylist`, `UrlImageCover`) VALUES
(1, '2021-04-09 18:17:00', '8ec98a070542086d414860c3269f3e87', 'Update playlist elangel', '/cover/test.jpg21');

-- --------------------------------------------------------

--
-- Table structure for table `playlist_following`
--

CREATE TABLE `playlist_following` (
  `idplaylist` int(11) NOT NULL,
  `iduser` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `playlist_song`
--

CREATE TABLE `playlist_song` (
  `idsong` int(11) NOT NULL,
  `idplaylist` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `regular_user`
--

CREATE TABLE `regular_user` (
  `IDuser` varchar(64) NOT NULL,
  `Gender` varchar(64) DEFAULT NULL,
  `DateofBirth` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `regular_user`
--

INSERT INTO `regular_user` (`IDuser`, `Gender`, `DateofBirth`) VALUES
('7d75b89967463de95fe37c55686a63e0', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `song`
--

CREATE TABLE `song` (
  `IDsong` int(11) NOT NULL,
  `IDalbum` int(11) NOT NULL,
  `Title` varchar(64) NOT NULL,
  `Urlsongs` varchar(64) NOT NULL,
  `Genre` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `song`
--

INSERT INTO `song` (`IDsong`, `IDalbum`, `Title`, `Urlsongs`, `Genre`) VALUES
(1, 1, 'Knock knock', 'http://knockknock.mp4', 'Happy'),
(2, 1, 'Fall Again', 'http://fallagain.mp4', 'Drama'),
(3, 2, 'Playing with fire', 'http://playingwithfire.mp4', 'Dance'),
(4, 2, 'Ice Cream', 'http://icecream.mp4', 'Happy');

-- --------------------------------------------------------

--
-- Table structure for table `song_like`
--

CREATE TABLE `song_like` (
  `iduser` varchar(64) NOT NULL,
  `idsong` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `IDuser` varchar(64) NOT NULL,
  `Username` varchar(64) NOT NULL,
  `Email` varchar(64) NOT NULL,
  `Password` varchar(256) NOT NULL,
  `Country` varchar(64) NOT NULL,
  `UrlPhotoProfile` varchar(255) NOT NULL,
  `DateJoin` datetime NOT NULL,
  `Categories` int(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`IDuser`, `Username`, `Email`, `Password`, `Country`, `UrlPhotoProfile`, `DateJoin`, `Categories`) VALUES
('0ee6d02530649ef3c3c2a81da45f72a0', 'iu', 'iu@gmail.com', '12345678', 'Korea', 'https://i.pinimg.com/564x/88/4c/67/884c6726d75b4639fbcbecf15743e568.jpg', '2022-03-20 11:52:32', 1),
('14681392a8f78ab96843bd595657a668', 'albertusaa', 'albertusaaa@gmail.com', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'Indonesia', 'https://www.princeton.edu/sites/default/files/styles/half_2x/public/images/2022/02/KOA_Nassau_2697x1517.jpg?itok=iQEwihUn', '2021-10-11 00:00:00', 2),
('365aaaf8834f326e5d3925cc82693605', 'blackpink', 'blackpink@gmail.com', '12345678', 'Korea', 'https://i.pinimg.com/564x/c8/64/17/c86417a904551d0bd685f5f2ebace360.jpg', '2022-03-20 11:57:11', 1),
('7d75b89967463de95fe37c55686a63e0', 'albetgf', 'dahdkj@gmail', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5', 'India', 'photo.jpg', '2021-10-11 00:00:00', 2),
('82921e4e534a3709e6dc6b1db41a1977', 'shaday', 'shaday@gmail.com', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'Indonesia', 'https://www.princeton.edu/sites/default/files/styles/half_2x/public/images/2022/02/KOA_Nassau_2697x1517.jpg?itok=iQEwihUn', '2022-01-11 00:00:00', 2),
('8ec98a070542086d414860c3269f3e87', 'elangel', 'elangel@gmail.com', 'e24df920078c3dd4e7e8d2442f00e5c9ab2a231bb3918d65cc50906e49ecaef4', 'Indonesia', 'https://www.princeton.edu/sites/default/files/styles/half_2x/public/images/2022/02/KOA_Nassau_2697x1517.jpg?itok=iQEwihUn', '2022-01-11 00:00:00', 2),
('9f44c64241f6095571dea1b73144a5fb', 'neilea', 'neilea@gmail.com', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'Indonesia', 'https://www.princeton.edu/sites/default/files/styles/half_2x/public/images/2022/02/KOA_Nassau_2697x1517.jpg?itok=iQEwihUn', '2022-01-11 00:00:00', 2);

-- --------------------------------------------------------

--
-- Table structure for table `user_following`
--

CREATE TABLE `user_following` (
  `userid` varchar(64) NOT NULL,
  `followinguserid` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user_history`
--

CREATE TABLE `user_history` (
  `id_user` varchar(64) NOT NULL,
  `id_list` varchar(64) NOT NULL,
  `type` varchar(64) NOT NULL,
  `date` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user_history`
--

INSERT INTO `user_history` (`id_user`, `id_list`, `type`, `date`) VALUES
('8ec98a070542086d414860c3269f3e87', '2', '2', '2021-04-25 03:43:40');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `album`
--
ALTER TABLE `album`
  ADD PRIMARY KEY (`IDalbum`);

--
-- Indexes for table `album_following`
--
ALTER TABLE `album_following`
  ADD PRIMARY KEY (`idalbum`,`iduser`),
  ADD KEY `fk_iduser_album_following` (`iduser`);

--
-- Indexes for table `artist`
--
ALTER TABLE `artist`
  ADD PRIMARY KEY (`IDuser`);

--
-- Indexes for table `playlist`
--
ALTER TABLE `playlist`
  ADD PRIMARY KEY (`IDplaylist`),
  ADD KEY `fk_iduser_playlist` (`IDuser`);

--
-- Indexes for table `playlist_following`
--
ALTER TABLE `playlist_following`
  ADD PRIMARY KEY (`idplaylist`,`iduser`),
  ADD KEY `fk_iduser_followingplaylist` (`iduser`);

--
-- Indexes for table `playlist_song`
--
ALTER TABLE `playlist_song`
  ADD PRIMARY KEY (`idsong`,`idplaylist`),
  ADD KEY `fk_idplaylist_songplaylist` (`idplaylist`);

--
-- Indexes for table `regular_user`
--
ALTER TABLE `regular_user`
  ADD PRIMARY KEY (`IDuser`);

--
-- Indexes for table `song`
--
ALTER TABLE `song`
  ADD PRIMARY KEY (`IDsong`),
  ADD KEY `fk_idalbum_song` (`IDalbum`);

--
-- Indexes for table `song_like`
--
ALTER TABLE `song_like`
  ADD PRIMARY KEY (`iduser`,`idsong`),
  ADD KEY `fk_idsong_likesong` (`idsong`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`IDuser`);

--
-- Indexes for table `user_following`
--
ALTER TABLE `user_following`
  ADD PRIMARY KEY (`userid`,`followinguserid`),
  ADD KEY `fk_followinguser` (`followinguserid`);

--
-- Indexes for table `user_history`
--
ALTER TABLE `user_history`
  ADD PRIMARY KEY (`id_user`,`id_list`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `playlist`
--
ALTER TABLE `playlist`
  MODIFY `IDplaylist` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `song`
--
ALTER TABLE `song`
  MODIFY `IDsong` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `album_following`
--
ALTER TABLE `album_following`
  ADD CONSTRAINT `fk_idalbum_album_following` FOREIGN KEY (`idalbum`) REFERENCES `album` (`IDalbum`),
  ADD CONSTRAINT `fk_iduser_album_following` FOREIGN KEY (`iduser`) REFERENCES `user` (`IDuser`);

--
-- Constraints for table `artist`
--
ALTER TABLE `artist`
  ADD CONSTRAINT `fk_iduser_artist` FOREIGN KEY (`IDuser`) REFERENCES `user` (`IDuser`);

--
-- Constraints for table `playlist`
--
ALTER TABLE `playlist`
  ADD CONSTRAINT `fk_iduser_playlist` FOREIGN KEY (`IDuser`) REFERENCES `user` (`IDuser`);

--
-- Constraints for table `playlist_following`
--
ALTER TABLE `playlist_following`
  ADD CONSTRAINT `fk_idplaylist_followingplaylist` FOREIGN KEY (`idplaylist`) REFERENCES `playlist` (`IDplaylist`),
  ADD CONSTRAINT `fk_iduser_followingplaylist` FOREIGN KEY (`iduser`) REFERENCES `user` (`IDuser`);

--
-- Constraints for table `playlist_song`
--
ALTER TABLE `playlist_song`
  ADD CONSTRAINT `fk_idplaylist_songplaylist` FOREIGN KEY (`idplaylist`) REFERENCES `playlist` (`IDplaylist`),
  ADD CONSTRAINT `fk_idsong_songplaylist` FOREIGN KEY (`idsong`) REFERENCES `song` (`IDsong`);

--
-- Constraints for table `regular_user`
--
ALTER TABLE `regular_user`
  ADD CONSTRAINT `fk_iduser` FOREIGN KEY (`IDuser`) REFERENCES `user` (`IDuser`);

--
-- Constraints for table `song`
--
ALTER TABLE `song`
  ADD CONSTRAINT `fk_idalbum_song` FOREIGN KEY (`IDalbum`) REFERENCES `album` (`IDalbum`);

--
-- Constraints for table `song_like`
--
ALTER TABLE `song_like`
  ADD CONSTRAINT `fk_idsong_likesong` FOREIGN KEY (`idsong`) REFERENCES `song` (`IDsong`),
  ADD CONSTRAINT `fk_iduser_likesong` FOREIGN KEY (`iduser`) REFERENCES `user` (`IDuser`);

--
-- Constraints for table `user_following`
--
ALTER TABLE `user_following`
  ADD CONSTRAINT `fk_followinguser` FOREIGN KEY (`followinguserid`) REFERENCES `user` (`IDuser`),
  ADD CONSTRAINT `fk_userid` FOREIGN KEY (`userid`) REFERENCES `user` (`IDuser`);

--
-- Constraints for table `user_history`
--
ALTER TABLE `user_history`
  ADD CONSTRAINT `fk_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`IDuser`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
