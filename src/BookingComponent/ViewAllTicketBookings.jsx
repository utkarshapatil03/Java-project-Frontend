import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const ViewAllTicketBookings = () => {
  const navigate = useNavigate();

  const [allBookings, setAllBookings] = useState([]);

  const retrieveAllBookings = async () => {
    const response = await axios.get(
      "http://localhost:9001/api/book/fetch/all"
    );
    return response.data;
  };

  useEffect(() => {
    const getAllBookings = async () => {
      const allBookings = await retrieveAllBookings();
      if (allBookings) {
        setAllBookings(allBookings.bookings);
      }
    };

    getAllBookings();
  }, []);

  return (
    <div className="mt-3">
      <div
        className="card form-card ms-2 me-2 mb-5 custom-bg border-color "
        style={{
          height: "45rem",
        }}
      >
        <div className="card-header custom-bg-text text-center bg-color">
          <h2>Customer Ticket Bookings</h2>
        </div>
        <div
          className="card-body"
          style={{
            overflowY: "auto",
          }}
        >
          <div className="table-responsive mt-3">
            <table className="table table-hover text-color text-center">
              <thead className="table-bordered border-color bg-color custom-bg-text">
                <tr>
                  <th scope="col">Train Name</th>
                  <th scope="col">Train No.</th>
                  <th scope="col">From</th>
                  <th scope="col">To</th>
                  <th scope="col">Schedule Timing</th>
                  <th scope="col">Fare Per Seat(In Rs)</th>
                  <th scope="col">Seat No.</th>
                  <th scope="col">Customer Name</th>
                  <th scope="col">Customer Mobile</th>
                  <th scope="col">Booking Id</th>
                  <th scope="col">Booking Timing</th>
                  <th scope="col">Booking Status</th>
                </tr>
              </thead>
              <tbody>
                {allBookings.map((booking) => {
                  return (
                    <tr>
                      <td>
                        <b>{booking.trainName}</b>
                      </td>
                      <td>
                        <b>{booking.trainNumber}</b>
                      </td>
                      <td>
                        <b>{booking.fromLocation}</b>
                      </td>
                      <td>
                        <b>{booking.toLocation}</b>
                      </td>
                      <td>
                        <b>{booking.scheduleTrainTime}</b>
                      </td>
                      <td>
                        <b>{booking.seatPrice}</b>
                      </td>
                      <td>
                        <b>{booking.trainSeat}</b>
                      </td>
                      <td>
                        <b>{booking.username}</b>
                      </td>
                      <td>
                        <b>{booking.mobile}</b>
                      </td>
                      <td>
                        <b>{booking.bookingId}</b>
                      </td>
                      <td>
                        <b>{booking.bookingTime}</b>
                      </td>
                      <td>
                        <b>{booking.status}</b>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewAllTicketBookings;
