-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 22, 2019 at 06:02 PM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `railway`
--

-- --------------------------------------------------------

--
-- Table structure for table `aadhaar`
--

CREATE TABLE `aadhaar` (
  `UID_NO` varchar(12) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `ADDRESS` text NOT NULL,
  `MOBILE_NO` varchar(15) NOT NULL,
  `DATE_OF_BIRTH` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `aadhaar`
--

INSERT INTO `aadhaar` (`UID_NO`, `NAME`, `ADDRESS`, `MOBILE_NO`, `DATE_OF_BIRTH`) VALUES
('743740988757', 'SHREYAS SHARAD PATIL', 'NIVRUTTI NAGAR, PIMPRALA, JALGAON', '+917767084933', '1999-05-12'),
('848396372700', 'MANISHA ANAND YEMUL', 'SOLAPUR , MAHARASHTRA 413005', '+918483963727', '1999-05-13'),
('855298600700', 'PIYUSH RAMAKANT RAJPUT', 'PUNE , MAHARASHTRA 411033', '+918552986007', '1997-11-10'),
('937202622400', 'JAGRUTI SATISH PATIL', 'JALGAON, MAHARASHTRA, 425001', '+919372026224', '1999-07-28');

-- --------------------------------------------------------

--
-- Table structure for table `alerts`
--

CREATE TABLE `alerts` (
  `alert_id` varchar(50) NOT NULL,
  `time` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `alerts`
--

INSERT INTO `alerts` (`alert_id`, `time`) VALUES
('9YuSrTsdOeQzwQAxsithwdENqw03', '2019-10-03 10:41:52'),
('g6saSSmI1MVi70wDZ4pCz6K0ntu2', '2019-10-06 11:34:17');

-- --------------------------------------------------------

--
-- Table structure for table `fir`
--

CREATE TABLE `fir` (
  `FIR_NO` bigint(20) NOT NULL,
  `UID` varchar(50) NOT NULL,
  `PNR_NO` varchar(10) NOT NULL,
  `LAST_STATION` varchar(10) NOT NULL,
  `CRIME` varchar(20) NOT NULL,
  `INFO` text NOT NULL,
  `STATUS` text NOT NULL,
  `TIMESTAMP` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fir`
--

INSERT INTO `fir` (`FIR_NO`, `UID`, `PNR_NO`, `LAST_STATION`, `CRIME`, `INFO`, `STATUS`, `TIMESTAMP`) VALUES
(2023, 'g6saSSmI1MVi70wDZ4pCz6K0ntu2', '9372026224', 'Daund', 'Dacoity', 'Some people attacked on passengers', 'APPROVED', '2019-10-03 10:02:41'),
(2025, '9YuSrTsdOeQzwQAxsithwdENqw03', '9372026224', 'daund', 'Accident', '', 'REJECTED', '2019-10-03 10:40:59'),
(2026, 'g6saSSmI1MVi70wDZ4pCz6K0ntu2', '7767084933', 'Pune', 'Theft', 'My bags stolen by some people.', 'APPROVED', '2019-10-06 11:34:06'),
(2027, 'g6saSSmI1MVi70wDZ4pCz6K0ntu2', '7767084933', 'Jalgaon', 'Theft', 'My mobile is stolen by some pocket cutters.', 'PENDING', '2019-10-22 10:17:34');

--
-- Triggers `fir`
--
DELIMITER $$
CREATE TRIGGER `StatusBinder` BEFORE INSERT ON `fir` FOR EACH ROW SET NEW.STATUS = 'PENDING'
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `reservation`
--

CREATE TABLE `reservation` (
  `TRAIN_NAME` varchar(20) NOT NULL,
  `TRAIN_NO` varchar(6) NOT NULL,
  `PNR_NO` varchar(10) NOT NULL,
  `SRC_STATION` varchar(20) NOT NULL,
  `DEST_STATION` varchar(20) NOT NULL,
  `SEAT_DETAIL` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `reservation`
--

INSERT INTO `reservation` (`TRAIN_NAME`, `TRAIN_NO`, `PNR_NO`, `SRC_STATION`, `DEST_STATION`, `SEAT_DETAIL`) VALUES
('Goa Express', '12780', '1425367890', 'Hzarat Nizamuddin', 'Vasco-da-Gama', 'S4/54'),
('KARNATAKA EXPRESS', '12640', '2387589328', 'DELHI', 'BANGLORE', 'S5/56'),
('Maharastra Express', '11040', '4596321716', 'Kolhapur', 'Jalgaon', 'S3/45'),
('NAVJEEVAN EXPRESS', '12096', '7767084933', 'CHENNAI', 'AHMEDABAD', 'S6/36'),
('PUNE - BSL EXPRESS', '11026', '8552986007', 'PUNE', 'BHUSAWAL', 'S4/12'),
('HUTATMA EXPRESS', '12157', '9372026224', 'SOLAPUR', 'PUNE', 'S5/23');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `UID` varchar(50) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `MOBILE_NO` varchar(30) NOT NULL,
  `AADHAAR_NO` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`UID`, `NAME`, `MOBILE_NO`, `AADHAAR_NO`) VALUES
('9YuSrTsdOeQzwQAxsithwdENqw03', 'MANISHA ANAND YEMUL', '+918483963727', '848396372700'),
('g6saSSmI1MVi70wDZ4pCz6K0ntu2', 'SHREYAS SHARAD PATIL', '+917767084933', '743740988757');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `aadhaar`
--
ALTER TABLE `aadhaar`
  ADD PRIMARY KEY (`UID_NO`);

--
-- Indexes for table `alerts`
--
ALTER TABLE `alerts`
  ADD PRIMARY KEY (`alert_id`);

--
-- Indexes for table `fir`
--
ALTER TABLE `fir`
  ADD PRIMARY KEY (`FIR_NO`),
  ADD KEY `PNR_NO` (`PNR_NO`),
  ADD KEY `fir_ibfk_2` (`UID`);

--
-- Indexes for table `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`PNR_NO`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`UID`),
  ADD KEY `AADHAAR_NO` (`AADHAAR_NO`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `fir`
--
ALTER TABLE `fir`
  MODIFY `FIR_NO` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2028;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `alerts`
--
ALTER TABLE `alerts`
  ADD CONSTRAINT `alerts_ibfk_1` FOREIGN KEY (`alert_id`) REFERENCES `user` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fir`
--
ALTER TABLE `fir`
  ADD CONSTRAINT `fir_ibfk_1` FOREIGN KEY (`PNR_NO`) REFERENCES `reservation` (`PNR_NO`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fir_ibfk_2` FOREIGN KEY (`UID`) REFERENCES `user` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`AADHAAR_NO`) REFERENCES `aadhaar` (`UID_NO`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
