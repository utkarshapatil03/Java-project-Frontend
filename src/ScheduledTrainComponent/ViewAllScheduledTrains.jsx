import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { useState, useEffect } from "react";

const ViewAllScheduledTrains = () => {
  const navigate = useNavigate();

  const [searchRequest, setSearchRequest] = useState({
    startDate: "",
    endDate: "",
    sourceLocationId: "",
    destinationLocationId: "",
  });

  const [tempSearchRequest, setTempSearchRequest] = useState({
    startDate: "",
    endDate: "",
    sourceLocationId: "",
    destinationLocationId: "",
  });

  const handleUserInput = (e) => {
    setSearchRequest({ ...searchRequest, [e.target.name]: e.target.value });
  };

  const handleTempSearchInput = (e) => {
    setTempSearchRequest({
      ...tempSearchRequest,
      [e.target.name]: e.target.value,
    });
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

  const [scheduledTrains, setScheduledTrains] = useState([]);

  const retrieveAllScheduledTrains = async () => {
    const response = await axios.get(
      "http://localhost:9003/api/train/scheduled/trains/all"
    );
    console.log(response.data);
    return response.data;
  };

  const searchScheduledTrainsByUserInput = async () => {
    const response = await axios.get(
      "http://localhost:9003/api/train/serach/scheduled/traind?startDate=" +
        convertToEpochTime(searchRequest.startDate) +
        "&endDate=" +
        convertToEpochTime(searchRequest.endDate) +
        "&sourceLocationId=" +
        searchRequest.sourceLocationId +
        "&destinationLocationId=" +
        searchRequest.destinationLocationId
    );
    console.log(response.data);
    return response.data;
  };

  const convertToEpochTime = (dateString) => {
    const selectedDate = new Date(dateString);
    const epochTime = selectedDate.getTime();
    return epochTime;
  };

  useEffect(() => {
    const getAllScheduledTrains = async () => {
      if (
        searchRequest.startDate &&
        searchRequest.endDate &&
        searchRequest.sourceLocationId &&
        searchRequest.destinationLocationId
      ) {
        const trains = await searchScheduledTrainsByUserInput();
        if (trains) {
          setScheduledTrains(trains.train);
        }
      } else {
        const trains = await retrieveAllScheduledTrains();
        if (trains) {
          setScheduledTrains(trains.train);
        }
      }
    };

    getAllScheduledTrains();
  }, [searchRequest]);

  const viewBookingDetails = (trainDetails) => {
    navigate("/schedule/train/ticket/check", { state: trainDetails });
  };

  const searchScheduledTrains = (e) => {
    setSearchRequest(tempSearchRequest);
    e.preventDefault();
  };

  return (
    <div className="mt-3">
      <div
        className="card form-card ms-2 me-2 mb-5 custom-bg border-color "
        style={{
          height: "45rem",
        }}
      >
        <div className="card-header custom-bg-text text-center bg-color">
          <h2>Scheduled Trains</h2>
        </div>
        <div
          className="card-body"
          style={{
            overflowY: "auto",
          }}
        >
          <div className="row">
            <div className="col">
              <form class="row g-3 align-items-center">
                <div class="col-auto">
                  <label>
                    <b>Select Start Date</b>
                  </label>
                  <input
                    type="date"
                    class="form-control"
                    name="startDate"
                    placeholder="Start Date..."
                    onChange={handleTempSearchInput}
                    value={tempSearchRequest.startDate}
                    required
                  />
                </div>
                <div class="col-auto">
                  <label>
                    <b>Select End Date</b>
                  </label>
                  <input
                    type="date"
                    class="form-control"
                    name="endDate"
                    placeholder="Start Date..."
                    onChange={handleTempSearchInput}
                    value={tempSearchRequest.endDate}
                    required
                  />
                </div>

                <div className="col-auto">
                  <label>
                    <b>From Location</b>
                  </label>
                  <select
                    onChange={handleTempSearchInput}
                    className="form-control"
                    name="sourceLocationId"
                    required
                  >
                    <option value="">Select Source Location</option>

                    {locations.map((location) => {
                      return (
                        <option value={location.id}> {location.name} </option>
                      );
                    })}
                  </select>
                </div>

                <div className="col-auto">
                  <label>
                    <b>To Location</b>
                  </label>
                  <select
                    onChange={handleTempSearchInput}
                    className="form-control"
                    name="destinationLocationId"
                    required
                  >
                    <option value="">Select Destination Location</option>

                    {locations.map((location) => {
                      return (
                        <option value={location.id}> {location.name} </option>
                      );
                    })}
                  </select>
                </div>

                <div class="col-auto">
                  <button
                    type="submit"
                    class="btn bg-color custom-bg-text btn-lg"
                    onClick={searchScheduledTrains}
                  >
                    Search
                  </button>
                </div>
              </form>
            </div>
          </div>
          <div className="table-responsive mt-3">
            <table className="table table-hover text-color text-center">
              <thead className="table-bordered border-color bg-color custom-bg-text">
                <tr>
                  <th scope="col">Scheduled Train Id</th>
                  <th scope="col">Train</th>
                  <th scope="col">Train No.</th>
                  <th scope="col">Fair (In Rs)</th>
                  <th scope="col">From Location</th>
                  <th scope="col">To Location</th>
                  <th scope="col">Timing</th>
                  <th scope="col">Action</th>
                </tr>
              </thead>
              <tbody>
                {scheduledTrains.map((train) => {
                  return (
                    <tr>
                      <td>
                        <b>{train.scheduleTrainId}</b>
                      </td>
                      <td>
                        <b>{train.name}</b>
                      </td>
                      <td>
                        <b>{train.number}</b>
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
                        <b>{train.scheduleTime}</b>
                      </td>

                      <td>
                        <button
                          onClick={() => viewBookingDetails(train)}
                          className="btn btn-sm bg-color custom-bg-text"
                        >
                          Check Ticket Availability
                        </button>
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

export default ViewAllScheduledTrains;
