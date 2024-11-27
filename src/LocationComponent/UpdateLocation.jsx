import { useState } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useNavigate, useLocation } from "react-router-dom";

const UpdateLocation = () => {
  const location = useLocation();
  const trainLocation = location.state;

  const [updateLocationRequest, setUpdateLocationRequest] =
    useState(trainLocation);

  const handleUserInput = (e) => {
    setUpdateLocationRequest({
      ...updateLocationRequest,
      [e.target.name]: e.target.value,
    });
  };

  const updateLocation = (e) => {
    fetch("http://localhost:9002/api/location/update", {
      method: "PUT",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(updateLocationRequest),
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
                window.location.href = "/view/locations";
              }, 2000); // Redirect after 3 seconds
          } else {
            console.log("Failed to fetch the locations");
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
    e.preventDefault();
  };

  return (
    <div>
      <div className="mt-2 d-flex aligns-items-center justify-content-center">
        <div
          className="card form-card border-color custom-bg"
          style={{ width: "25rem" }}
        >
          <div className="card-header bg-color text-center custom-bg-text">
            <h4 className="card-title">Update Location</h4>
          </div>
          <div className="card-body">
            <form>
              <div className="mb-3 text-color">
                <label for="name" class="form-label">
                  <b>Location</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="name"
                  name="name"
                  onChange={handleUserInput}
                  value={updateLocationRequest.name}
                  required
                />
              </div>
              <div class="mb-3 text-color">
                <label for="description" class="form-label">
                  <b>Description</b>
                </label>
                <textarea
                  class="form-control"
                  id="description"
                  rows="3"
                  name="description"
                  placeholder="enter description.."
                  onChange={handleUserInput}
                  value={updateLocationRequest.description}
                />
              </div>
              <button
                type="submit"
                className="btn bg-color custom-bg-text"
                onClick={updateLocation}
              >
                Update Location
              </button>
              <ToastContainer />
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UpdateLocation;
