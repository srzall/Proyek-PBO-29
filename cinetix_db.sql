-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 04, 2025 at 05:51 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cinetix_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `bookings`
--

CREATE TABLE `bookings` (
  `booking_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `movie_id` int(11) DEFAULT NULL,
  `seat_number` varchar(5) NOT NULL,
  `showtime` varchar(10) DEFAULT NULL,
  `booking_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bookings`
--

INSERT INTO `bookings` (`booking_id`, `user_id`, `movie_id`, `seat_number`, `showtime`, `booking_date`) VALUES
(20, 1, 1, 'A2', '10.00', '2025-11-25 14:49:05'),
(21, 1, 2, 'B1', '10.00', '2025-11-25 14:51:32'),
(22, 1, 2, 'C3', '10.00', '2025-11-26 03:15:37'),
(30, 1, 19, 'A6', '10.00', '2025-12-01 06:53:25'),
(31, 10, 1, 'A4', '10.00', '2025-12-01 06:59:23'),
(32, 10, 2, 'A6', '10.00', '2025-12-01 07:00:04'),
(33, 1, 2, 'A4', '10.00', '2025-12-02 06:37:15'),
(34, 1, 1, 'A4', '10.30', '2025-12-02 16:24:55');

-- --------------------------------------------------------

--
-- Table structure for table `movies`
--

CREATE TABLE `movies` (
  `movie_id` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `genre` varchar(50) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `trailer_path` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `rating` varchar(10) DEFAULT '? 4.5',
  `duration` varchar(20) DEFAULT '2h 0m',
  `showtimes` varchar(255) DEFAULT '10.00,13.00,16.00,19.00,21.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `movies`
--

INSERT INTO `movies` (`movie_id`, `title`, `genre`, `price`, `image_path`, `trailer_path`, `description`, `rating`, `duration`, `showtimes`) VALUES
(1, 'Spider-Man: No Way Home', 'Action', 45000.00, 'assets/images/spiderman.png', 'assets/videos/spiderman.mp4', 'Identitas Spider-Man terungkap. Peter meminta bantuan Doctor Strange, namun mantra yang salah membuka multiverse dan mengundang musuh-musuh berbahaya.', ' 4.5', '2h 0m', '10.30, 13.00, 15.30, 18.00, 20.30'),
(2, 'Barbie', 'Drama/Comedy', 40000.00, 'assets/images/barbie.png', 'assets/videos/barbie.mp4', 'Barbie mengalami krisis eksistensi dan melakukan perjalanan dari Barbieland ke dunia nyata untuk menemukan jati dirinya.', ' 4.5', '2h 0m', '10.00,13.00,16.00'),
(13, 'Oppenheimer', 'Biography', 50000.00, 'assets/images/oppenheimer.png', 'assets/videos/oppenheimer.mp4', 'Kisah epik J. Robert Oppenheimer, fisikawan teoretis yang memimpin Proyek Manhattan untuk menciptakan bom atom pertama yang mengubah dunia selamanya.', '8.9', '3h 0m', '11.00, 15.00, 19.00'),
(14, 'The Nun II', 'Horror', 45000.00, 'assets/images/nun.png', 'assets/videos/nun.mp4', 'Berlatar tahun 1956 di Prancis, seorang pendeta dibunuh. Suster Irene sekali lagi berhadapan dengan kekuatan jahat Valak, sang biarawati iblis, yang meneror sebuah sekolah asrama.', ' 5.6', '1h 50m', '17.30, 19.45, 22.00'),
(15, 'Avatar: The Way of Water', 'Sci-Fi', 50000.00, 'assets/images/avatar2.png', 'assets/videos/avatar2.mp4', 'Jake Sully dan Neytiri kini memiliki keluarga. Namun, ancaman lama kembali, memaksa mereka meninggalkan rumah dan mencari perlindungan di klan Metkayina yang hidup selaras dengan lautan Pandora.', ' 7.6', '3h 12m', '10.00, 14.00, 18.00'),
(16, 'The Super Mario Bros. Movie', 'Animation', 40000.00, 'assets/images/mario.png', 'assets/videos/mario.mp4', 'Tukang ledeng Mario berpetualang melalui Kerajaan Jamur yang ajaib bersama Putri Peach dan Toad untuk menyelamatkan saudaranya, Luigi, dari cengkeraman Bowser yang jahat.', ' 7.1', '1h 32m', '09.00, 11.00, 13.00, 15.00, 17.00'),
(17, 'John Wick: Chapter 4', 'Action', 45000.00, 'assets/images/johnwick4.png', 'assets/videos/johnwick4.mp4', 'John Wick menemukan jalan untuk mengalahkan High Table. Namun sebelum ia bisa mendapatkan kebebasannya, ia harus berhadapan dengan musuh baru dengan aliansi global yang kuat.', ' 4.5', '2h 0m', '12.00, 15.15, 18.30, 21.45'),
(18, 'Spider-Man: Across the Spider-Verse', 'Animation', 45000.00, 'assets/images/spiderverse.png', 'assets/videos/spiderverse.mp4', 'Miles Morales melintasi Multiverse dan bertemu dengan tim Spider-People yang bertugas melindungi keberadaannya. Namun, ia bentrok dengan mereka tentang cara menangani ancaman baru.', '4.5', '2h 0m', '10.30, 13.00, 15.30, 18.00, 20.30'),
(19, 'Dune: Part Two', 'Sci-Fi/Adventure', 50000.00, 'assets/images/dune2.png', 'assets/videos/dune2.mp4', 'Paul Atreides bersatu dengan Chani dan kaum Fremen saat ia berada di jalur perang untuk membalas dendam terhadap para konspirator yang menghancurkan keluarganya.', '4.5', '2h 0m', '12.15, 14.45, 17.15, 19.45'),
(20, 'Wonka', 'Adventure/Comedy', 40000.00, 'assets/images/wonka.png', 'assets/videos/wonka.mp4', 'Kisah ajaib tentang bagaimana Willy Wonka muda, penuh dengan ide dan tekad, berjuang melawan kartel cokelat untuk membangun pabrik cokelat paling terkenal di dunia.', '4.5', '2h 0m', '10.00, 12.30, 15.00, 17.30, 20.00'),
(21, 'The Batman', 'Action/Crime', 45000.00, 'assets/images/batman.png', 'assets/videos/batman.mp4', 'Ketika The Riddler, seorang pembunuh berantai sadis mulai membunuh tokoh-tokoh politik penting di Gotham, Batman terpaksa menyelidiki korupsi tersembunyi kota tersebut dan mempertanyakan sejarah keluarganya.', ' 4.5', '2h 0m', '13.00, 16.00, 19.00, 22.00'),
(22, 'Top Gun: Maverick', 'Action', 45000.00, 'assets/images/topgun.png', 'assets/videos/topgun.mp4', 'Setelah lebih dari tiga puluh tahun mengabdi sebagai salah satu penerbang top Angkatan Laut, Pete \"Maverick\" Mitchell kembali untuk melatih lulusan TOP GUN untuk misi khusus yang belum pernah ada sebelumnya.', ' 4.5', '2h 0m', '11.15, 14.15, 17.15, 20.15');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`) VALUES
(1, 'fia', '123'),
(4, 'bayu', '777'),
(5, 'fajar', '999'),
(6, 'izzat', '999'),
(7, 'alex', '000'),
(9, 'ceira', '12345'),
(10, 'rizal', '123');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`booking_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `movie_id` (`movie_id`);

--
-- Indexes for table `movies`
--
ALTER TABLE `movies`
  ADD PRIMARY KEY (`movie_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bookings`
--
ALTER TABLE `bookings`
  MODIFY `booking_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT for table `movies`
--
ALTER TABLE `movies`
  MODIFY `movie_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`movie_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
