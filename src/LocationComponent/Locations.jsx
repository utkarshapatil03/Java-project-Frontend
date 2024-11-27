import { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Locations = () => {
  const [allLocations, setAllLocations] = useState([]);
  let navigate = useNavigate();

  const [addLocationrequest, setAddLocationrequest] = useState({});

  const handleUserInput = (e) => {
    setAddLocationrequest({
      ...addLocationrequest,
      [e.target.name]: e.target.value,
    });
  };

  useEffect(() => {
    const getAllLocations = async () => {
      const allLocations = await retrieveAllLocation();
      if (allLocations) {
        setAllLocations(allLocations.location);
      }
    };

    getAllLocations();
  }, []);

  const retrieveAllLocation = async () => {
    const response = await axios.get("http://localhost:9002/api/location/all");
    console.log(response.data);
    return response.data;
  };

  const addLocation = (e) => {
    fetch("http://localhost:9002/api/location/add", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(addLocationrequest),
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
            console.log("Failed to add the location");
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

  const updateLocation = (location) => {
    navigate("/location/update", { state: location });
  };

  const deleteLocation = (locationId) => {
    fetch("http://localhost:9002/api/location/delete?locatonId=" + locationId, {
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
            console.log("Failed to delete the location");
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
      <div className="row">
        <div className="col-md-4 ms-2">
          <div className="mt-2 d-flex aligns-items-center justify-content-center">
            <div
              className="card form-card border-color custom-bg"
              style={{ width: "25rem" }}
            >
              <div className="card-header bg-color text-center custom-bg-text">
                <h4 className="card-title">Add Location</h4>
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
                      value={addLocationrequest.name}
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
                      value={addLocationrequest.description}
                    />
                  </div>
                  <button
                    type="submit"
                    className="btn bg-color custom-bg-text"
                    onClick={addLocation}
                  >
                    Add Location
                  </button>
                  <ToastContainer />
                </form>
              </div>
            </div>
          </div>
        </div>
        <div className="col-md-7">
          <div className="mt-2">
            <div
              className="card form-card ms-5 me-5 mb-5 custom-bg border-color "
              style={{
                height: "30rem",
              }}
            >
              <div className="card-header custom-bg-text text-center bg-color">
                <h2>All Locations</h2>
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
                        <th scope="col">Location</th>
                        <th scope="col">Description</th>
                        <th scope="col">Action</th>
                      </tr>
                    </thead>
                    <tbody>
                      {allLocations.map((location) => {
                        return (
                          <tr>
                            <td>
                              <b>{location.name}</b>
                            </td>
                            <td>
                              <b>{location.description}</b>
                            </td>

                            <td>
                              <button
                                onClick={() => deleteLocation(location.id)}
                                className="btn btn-sm bg-color custom-bg-text"
                              >
                                Remove
                              </button>

                              <button
                                onClick={() => updateLocation(location)}
                                className="btn btn-sm bg-color custom-bg-text ms-1"
                              >
                                Update
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
      </div>
    </div>
  );
};

export default Locations;
