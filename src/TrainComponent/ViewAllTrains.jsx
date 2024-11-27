import { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const ViewAllTrains = () => {
  let navigate = useNavigate();
  const [allTrains, setAllTrains] = useState([]);

  const retrieveAllTrains = async () => {
    const response = await axios.get("http://localhost:9003/api/train/fetch/all");
    console.log(response.data);
    return response.data;
  };

   useEffect(() => {
     const getAllTrains = async () => {
       const allTrains = await retrieveAllTrains();
       if (allTrains) {
         setAllTrains(allTrains.train);
       }
     };

     getAllTrains();
   }, []);

  const deleteTrain = (trainId) => {
    fetch("http://localhost:9003/api/train/delete?trainId=" + trainId, {
      method: "DELETE",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    })
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

  return (
    <div>
      <div className="mt-2">
        <div
          className="card form-card ms-5 me-5 mb-5 custom-bg border-color "
          style={{
            height: "30rem",
          }}
        >
          <div className="card-header custom-bg-text text-center bg-color">
            <h2>All Trains</h2>
          </div>
          <div
            className="card-body"
            style={{
              overflowY: "auto",
            }}
          >
            <div className="table-responsive">
              <table className="table table-hover text-color text-center">
                <thead className="table-bordered border-color bg-color custom-bg-text">
                  <tr>
                    <th scope="col">Train</th>
                    <th scope="col">Train Number</th>
                    <th scope="col">Total Coach</th>
                    <th scope="col">Total Seat In Each Coach</th>
                    <th scope="col">Total Price</th>
                    <th scope="col">Source Location</th>
                    <th scope="col">Destination Location</th>
                    <th scope="col">Action</th>
                  </tr>
                </thead>
                <tbody>
                  {allTrains.map((train) => {
                    return (
                      <tr>
                        <td>
                          <b>{train.name}</b>
                        </td>
                        <td>
                          <b>{train.number}</b>
                        </td>
                        <td>
                          <b>{train.totalCoach}</b>
                        </td>
                        <td>
                          <b>{train.totalSeatInEachCoach}</b>
                        </td>
                        <td>
                          <b>{train.seatPrice}</b>
                        </td>
                        <td>
                          <b>{train.fromLocation}</b>
                        </td>
                        <td>
                          <b>{train.toLocation}</b>
                        </td>

                        <td>
                          <button
                            onClick={() => deleteTrain(train.id)}
                            className="btn btn-sm bg-color custom-bg-text"
                          >
                            Remove
                          </button>
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
    </div>
  );
};

export default ViewAllTrains;
