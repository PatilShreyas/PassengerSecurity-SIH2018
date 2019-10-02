-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 02, 2019 at 07:15 PM
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
('279467836433', 'ANKIT SHRIRAM PATIL', 'HIVARKHEDE BK., TAL JAMNER, DIST JALGAON', '+918698502533', '1999-04-22'),
('526077942889', 'PREM RAMESH CHAVHAN', 'WANKHEDE COLONY,JALGAON', '+919028723187', '1999-09-10'),
('591774672168', 'RUCHITA YOGESH BHOGE', 'VIDYUT COLONY, JALGAON', '+917057594556', '1999-05-29'),
('636308438360', 'ROHIT DATTATRAYA BHOKARIKAR', 'HARESHWAR NAGAR,RING ROAD,JALGAON', '+919405110018', '1999-05-04'),
('743740988757', 'SHREYAS SHARAD PATIL', 'NIVRUTTI NAGAR, PIMPRALA, JALGAON', '+917767084933', '1999-05-12'),
('918340931473', 'CHETANA HITENDRA PATIL', 'VIVARE KH, TAL. RAVER, DIST. JALGAON', '+919765293672', '1999-07-08');

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
('g6saSSmI1MVi70wDZ4pCz6K0ntu2', '2019-10-02 16:58:44');

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
(2019, 'g6saSSmI1MVi70wDZ4pCz6K0ntu2', '7767084933', 'Jalgaon', 'Suspecious Thing/Per', '', 'PENDING', '2019-10-02 06:50:01'),
(2020, 'g6saSSmI1MVi70wDZ4pCz6K0ntu2', '7767084933', 'Jalgaon', 'Theft', 'jdjkd.', 'APPROVED', '2019-10-02 07:40:22'),
(2021, 'g6saSSmI1MVi70wDZ4pCz6K0ntu2', '1425367890', 'Jalgaon', 'Murder', '', 'REJECTED', '2019-10-02 11:01:23'),
(2022, 'g6saSSmI1MVi70wDZ4pCz6K0ntu2', '7767084933', 'Pune', 'Theft', '', 'PENDING', '2019-10-02 14:53:40');

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
('Maharastra Express', '11040', '4596321716', 'Kolhapur', 'Jalgaon', 's3/45'),
('NAVJEEVAN EXPRESS', '12096', '7767084933', 'CHENNAI', 'AHMEDABAD', 'S6/36');

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
('62KqPSqvxrRLPx31xR25HmWkHdB2', 'CHETANA HITENDRA PATIL', '+919765293672', '918340931473'),
('a3gypodEThZgepwhvXkEM5jOvIt2', 'SHREYAS SHARAD PATIL', '+917767084933', '743740988757'),
('DFyvjRMCapZUQEZ96Kse0NTkE6P2', 'SHREYAS SHARAD PATIL', '+917767084933', '743740988757'),
('g6saSSmI1MVi70wDZ4pCz6K0ntu2', 'SHREYAS SHARAD PATIL', '+917767084933', '743740988757');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `aadhaar`
--
ALTER TABLE `aadhaar`
  ADD PRIMARY KEY (`UID_NO`),
  ADD UNIQUE KEY `UID_NO` (`UID_NO`);

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
  MODIFY `FIR_NO` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2023;

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
