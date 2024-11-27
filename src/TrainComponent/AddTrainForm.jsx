import { useState, useEffect } from "react";
import "react-toastify/dist/ReactToastify.css";
import { ToastContainer, toast } from "react-toastify";
import axios from "axios";
import { resolvePath, useNavigate } from "react-router-dom";

const AddTrainForm = () => {
  const navigate = useNavigate();

  const [train, setTrain] = useState({
    name: "",
    number: "",
    totalCoach: "",
    totalSeatInEachCoach: "",
    seatPrice: "",
    fromLocationId: "",
    toLocationId: "",
  });

  const handleTrainInput = (e) => {
    setTrain({ ...train, [e.target.name]: e.target.value });
  };

  const [locations, setLocations] = useState([]);

  const retrieveAllLocations = async () => {
    const response = await axios.get("http://localhost:9002/api/location/all");
    return response.data;
  };

  useEffect(() => {
    const getAllLocations = async () => {
      const allLocations = await retrieveAllLocations();
      if (allLocations) {
        setLocations(allLocations.location);
      }
    };

    getAllLocations();
  }, []);

  const saveTrain = (e) => {
    
    fetch("http://localhost:9003/api/train/register", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(train),
    })
      .then((result) => {
        console.log("result", result);
        result.json().then((res) => {
          console.log(res);

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
            console.log("Didn't got success response");
            toast.error("It seems server is down", {
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
        setTimeout(() => {
          window.location.reload(true);
        }, 1000); // Redirect after 3 seconds
      });
    e.preventDefault();
  
  };

  return (
    <div>
      <div className="mt-2 d-flex aligns-items-center justify-content-center ms-2 me-2 mb-2">
        <div
          className="card form-card border-color text-color custom-bg"
          style={{ width: "50rem" }}
        >
          <div className="card-header bg-color custom-bg-text text-center">
            <h5 className="card-title">Add Train</h5>
          </div>
          <div className="card-body">
            <form className="row g-3" onSubmit={saveTrain}>
              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b> Train Name</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="name"
                  name="name"
                  onChange={handleTrainInput}
                  value={train.name}
                />
              </div>
              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="description" className="form-label">
                  <b>Train Number</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="number"
                  name="number"
                  onChange={handleTrainInput}
                  value={train.number}
                />
              </div>
              <div className="col-md-6 mb-3 text-color">
                <b>
                  <label className="form-label">Total Coach</label>
                </b>
                <input
                  type="number"
                  className="form-control"
                  id="totalCoach"
                  name="totalCoach"
                  onChange={handleTrainInput}
                  value={train.totalCoach}
                />
              </div>
              <div className="col-md-6 mb-3">
                <label htmlFor="quantity" className="form-label">
                  <b>Total Seat In Each Coach</b>
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="totalSeatInEachCoach"
                  name="totalSeatInEachCoach"
                  onChange={handleTrainInput}
                  value={train.totalSeatInEachCoach}
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="fromLocationId" className="form-label">
                  <b>Source Location</b>
                </label>
                <select
                  onChange={handleTrainInput}
                  className="form-control"
                  name="fromLocationId"
                >
                  <option value="">Select Source Location</option>

                  {locations.map((location) => {
                    return (
                      <option value={location.id}> {location.name} </option>
                    );
                  })}
                </select>
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="toLocationId" className="form-label">
                  <b>Destination Location</b>
                </label>
                <select
                  onChange={handleTrainInput}
                  className="form-control"
                  name="toLocationId"
                >
                  <option value="">Select Destination Location</option>

                  {locations.map((location) => {
                    return (
                      <option value={location.id}> {location.name} </option>
                    );
                  })}
                </select>
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="seatPrice" className="form-label">
                  <b>Seat Price</b>
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="seatPrice"
                  name="seatPrice"
                  onChange={handleTrainInput}
                  value={train.seatPrice}
                />
              </div>

              <div className="d-flex aligns-items-center justify-content-center">
                <input
                  type="submit"
                  className="btn bg-color custom-bg-text"
                  value="Add Train"
                />
              </div>
              <ToastContainer />
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddTrainForm;
