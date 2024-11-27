import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const ViewMyBookings = () => {
  const navigate = useNavigate();

  var customer = JSON.parse(sessionStorage.getItem("active-customer"));

  const [myBookings, setMyBookings] = useState([]);

  const retrieveMyBookings = async () => {
    const response = await axios.get(
      "http://localhost:9001/api/book/fetch/user?userId=" + customer.id
    );
    return response.data;
  };

  useEffect(() => {
    const getMyBookings = async () => {
      const myBookings = await retrieveMyBookings();
      if (myBookings) {
        setMyBookings(myBookings.bookings);
      }
    };

    getMyBookings();
  }, []);

  const cancelTrainTicket = (bookingId) => {
    fetch(
      "http://localhost:9001/api/book/ticket/cancel?bookingId=" + bookingId,
      {
        method: "DELETE",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/js on",
        },
      }
    )
      .then((result) => {
        result.json().then((res) => {
          if (res.success) {
            console.log("Got the success response");

            toast.success(res.responseMessage, {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });

            setTimeout(() => {
              window.location.reload(true);
            }, 1000); // Redirect after 3 seconds
          } else {
            console.log("Failed to delete the Train");
            toast.error("It seems server is down", {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
          }
        });
      })
      .catch((error) => {
        console.error(error);
        toast.error("It seems server is down", {
          position: "top-center",
          autoClose: 1000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
      });

    setTimeout(() => {
      window.location.reload(true);
    }, 2000); // Reload after 3 seconds 3000
  };

  const downloadTicket = (bookingId) => {
    fetch(`http://localhost:9001/api/book/download/ticket?bookingId=${bookingId}`)
      .then(response => response.blob())
      .then(blob => {
        // Create a temporary URL for the blob
        const url = window.URL.createObjectURL(blob);

        // Create a temporary <a> element to trigger the download
        const link = document.createElement('a');
        link.href = url;
        link.download = 'ticket.pdf'; // Specify the desired filename here

        // Append the link to the document and trigger the download
        document.body.appendChild(link);
        link.click();

        // Clean up the temporary URL and link
        URL.revokeObjectURL(url);
        document.body.removeChild(link);
      })
      .catch(error => {
        console.error('Download error:', error);
      });
  }

  return (
    <div className="mt-3">
      <div
        className="card form-card ms-2 me-2 mb-5 custom-bg border-color "
        style={{
          height: "45rem",
        }}
      >
        <div className="card-header custom-bg-text text-center bg-color">
          <h2>My Train Bookings</h2>
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
                  <th scope="col">Booking Id</th>
                  <th scope="col">Booking Timing</th>
                  <th scope="col">Booking Status</th>
                  <th scope="col">Action</th>
                </tr>
              </thead>
              <tbody>
                {myBookings.map((booking) => {
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
                        <b>{booking.bookingId}</b>
                      </td>
                      <td>
                        <b>{booking.bookingTime}</b>
                      </td>
                      <td>
                        <b>{booking.status}</b>
                      </td>

                      <td>
                        

                        {(() => {
                          if (booking.status !== "Cancelled") {
                            return (
                              <button
                                onClick={() =>
                                  cancelTrainTicket(booking.bookId)
                                }
                                className="btn btn-sm bg-color custom-bg-text"
                              >
                                Cancel Ticket
                              </button>
                            );
                          }
                        })()}

                        {(() => {
                          if (booking.status === "Confirmed") {
                            return (
                              <button
                                onClick={() =>
                                  downloadTicket(booking.bookingId)
                                }
                                className="btn btn-sm bg-color custom-bg-text ms-2"
                              >
                                Download Ticket
                              </button>
                            );
                          }
                        })()}

                        <ToastContainer />
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

export default ViewMyBookings;
