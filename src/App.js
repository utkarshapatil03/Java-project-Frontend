import "./App.css";
import { Route, Routes } from "react-router-dom";
import AboutUs from "./page/AboutUs";
import ContactUs from "./page/ContactUs";
import Header from "./NavbarComponent/Header";
import HomePage from "./page/HomePage";
import UserRegister from "./UserComponent/UserRegister";
import UserLoginForm from "./UserComponent/UserLoginForm";
import ViewAllScheduledTrains from "./ScheduledTrainComponent/ViewAllScheduledTrains";
import Locations from "./LocationComponent/Locations";
import UpdateLocation from "./LocationComponent/UpdateLocation";
import AddTrainForm from "./TrainComponent/AddTrainForm";
import ViewAllTrains from "./TrainComponent/ViewAllTrains";
import ScheduleTrainForm from "./ScheduledTrainComponent/ScheduleTrainForm";
import ViewScheduledTrainBookings from "./BookingComponent/ViewScheduledTrainBookings";
import BookTrainTicketForm from "./BookingComponent/BookTrainTicketForm";
import ViewMyBookings from "./BookingComponent/ViewMyBookings";
import ViewAllTicketBookings from "./BookingComponent/ViewAllTicketBookings";
import AdminRegisterForm from "./UserComponent/AdminRegisterForm";

function App() {
  return (
    <div>
      <Header />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/home/all/hotel/location" element={<HomePage />} />
        <Route path="contact" element={<ContactUs />} />
        <Route path="about" element={<AboutUs />} />
        <Route path="/user/customer/register" element={<UserRegister />} />
        <Route path="/user/manager/register" element={<UserRegister />} />
        <Route path="/user/login" element={<UserLoginForm />} />
        <Route
          path="/scheduled/train/all"
          element={<ViewAllScheduledTrains />}
        />
        <Route path="/view/locations" element={<Locations />} />
        <Route path="/location/update" element={<UpdateLocation />} />
        <Route path="/train/add" element={<AddTrainForm />} />
        <Route path="/train/view" element={<ViewAllTrains />} />
        <Route path="/train/schedule" element={<ScheduleTrainForm />} />
        <Route
          path="/schedule/train/ticket/check"
          element={<ViewScheduledTrainBookings />}
        />
        <Route
          path="/schedule/train/book/ticket/"
          element={<BookTrainTicketForm />}
        />
        <Route path="/mybooking/ticket" element={<ViewMyBookings />} />
        <Route
          path="/customer/ticket/bookings"
          element={<ViewAllTicketBookings />}
        />
        <Route path="/user/admin/register" element={<AdminRegisterForm />} />
      </Routes>
    </div>
  );
}

export default App;
