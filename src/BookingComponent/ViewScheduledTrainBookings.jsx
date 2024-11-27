import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { useState, useEffect } from "react";

const ViewScheduledTrainBookings = () => {
  const navigate = useNavigate();

  const location = useLocation();
  const trainDetail = location.state;

  const [scheduledTrainBookings, setScheduledTrainBookings] = useState([]);
  const [ticketCounts, setTicketCounts] = useState({});

  const retrieveScheduleTrainBookings = async () => {
    const response = await axios.get(
      "http://localhost:9001/api/book/fetch/scheduled/train/tickets?scheduleTrainId=" +
        trainDetail.scheduleId
    );
    console.log(response.data);
    return response.data;
  };

  useEffect(() => {
    const getScheduleTrainBookings = async () => {
      const trainTickets = await retrieveScheduleTrainBookings();
      if (trainTickets) {
        setScheduledTrainBookings(trainTickets.bookings);
      }
    };

    getScheduleTrainBookings();
  }, []);

  const retrieveScheduleTrainTicketCounts = async () => {
    const response = await axios.get(
      "http://localhost:9001/api/book/fetch/schedule/train/ticket/count?scheduleTrainId=" +
        trainDetail.scheduleId
    );
    console.log(response.data);
    return response.data;
  };

  useEffect(() => {
    const getScheduleTrainTicketCounts = async () => {
      const trainTicketCounts = await retrieveScheduleTrainTicketCounts();
      if (trainTicketCounts) {
        setTicketCounts(trainTicketCounts);
      }
    };

    getScheduleTrainTicketCounts();
  }, []);

  const bookTrainTicket = () => {
      navigate("/schedule/train/book/ticket", { state: trainDetail }
      );
  };

  return (
    <div className="mt-3">
      <div className="d-flex justify-content-center align-items-center">
        <div
          className="card form-card ms-2 me-2 mb-5 custom-bg border-color "
          style={{
            height: "45rem",
            width: "40rem",
          }}
        >
          <div className="card-header custom-bg-text text-center bg-color">
            <h2>Check Train Ticket Availablity</h2>
          </div>
          <div className="card-body" style={{ overflowY: "auto" }}>
            <div className="row">
              <div className="col-md-6">
                <b>Train Name:</b>
                <h5 className="text-color"> {trainDetail.name}</h5>
                <div className="mt-3">
                  <b>Train Number:</b>
                  <h5 className="text-color"> {trainDetail.number}</h5>
                </div>
                <div className="mt-3">
                  <b>Schedule Train Id:</b>
                  <h5 className="text-color"> {trainDetail.scheduleTrainId}</h5>
                </div>
                <div className="mt-3">
                  <b>Per Seat Price (in Rs):</b>
                  <h5 className="text-color"> {trainDetail.seatPrice}</h5>
                </div>
                <div className="mt-3">
                  <b>Train Schedule Timing:</b>
                  <h5 className="text-color"> {trainDetail.scheduleTime}</h5>
                </div>
                <hr className="my-4" />
                <div className="mt-3">
                  <b>Total Available Seat:</b>
                  <h5 className="text-color">
                    {ticketCounts.availableTicketCount}
                  </h5>
                </div>
                <div className="mt-3">
                  <b>Total Confirmed Seat:</b>
                  <h5 className="text-color">
                    {ticketCounts.confirmedTicketCount}
                  </h5>
                </div>
                <div className="mt-3">
                  <b>Total Waiting Seat:</b>
                  <h5 className="text-color">
                    {ticketCounts.waitingTicketCount}
                  </h5>
                </div>
              </div>
              <div className="col-md-5">
                <div className="table-responsive" style={{ float: "right" }}>
                  <div className="text-center">
                    <h4 className="text-color">Train Seats</h4>
                  </div>
                  <table className="table table-hover text-color text-center">
                    <thead className="table-bordered border-color bg-color custom-bg-text">
                      <tr>
                        <th scope="col">Train Seat</th>
                        <th scope="col">Booking Status</th>
                      </tr>
                    </thead>
                    <tbody>
                      {scheduledTrainBookings.map((booking) => {
                        return (
                          <tr>
                            <td>
                              <b>{booking.trainSeat}</b>
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

            <div className="mt-3 d-flex justify-content-center align-items-center">
              <button
                onClick={() => bookTrainTicket()}
                className="btn btn-lg bg-color custom-bg-text"
              >
                Book Ticket
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewScheduledTrainBookings;
