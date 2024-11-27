import { useState, useEffect } from "react";
import "react-toastify/dist/ReactToastify.css";
import { ToastContainer, toast } from "react-toastify";
import axios from "axios";
import { resolvePath, useNavigate } from "react-router-dom";

const ScheduleTrainForm = () => {
  const navigate = useNavigate();

  const [scheduledTime, setScheduledTime] = useState("");

  const [scheduleTrainRequest, setScheduleTrainRequest] = useState({
    trainId: "",
    scheduleTime: "",
  });

  const handleUserInput = (e) => {
    setScheduleTrainRequest({
      ...scheduleTrainRequest,
      [e.target.name]: e.target.value,
    });
  };

  const [allTrains, setAllTrains] = useState([]);

  const retrieveAllTrains = async () => {
    const response = await axios.get(
      "http://localhost:9003/api/train/fetch/all"
    );
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

  const scheduleTrain = (e) => {

    const epochTime = new Date(scheduledTime).getTime();
    console.log(epochTime);
    scheduleTrainRequest.scheduleTime = epochTime;

    fetch("http://localhost:9003/api/train/schedule", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(scheduleTrainRequest),
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
            <h5 className="card-title">Schedule Train</h5>
          </div>
          <div className="card-body">
            <form className="row g-3" onSubmit={scheduleTrain}>
              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="trainId" className="form-label">
                  <b>Train to Schedule</b>
                </label>
                <select
                  onChange={handleUserInput}
                  className="form-control"
                  name="trainId"
                >
                  <option value="">Select Train</option>

                  {allTrains.map((train) => {
                    return <option value={train.id}> {train.name} </option>;
                  })}
                </select>
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Select Time</b>
                </label>
                <input
                  type="datetime-local"
                  className="form-control"
                  value={scheduledTime}
                  onChange={(e) => setScheduledTime(e.target.value)}
                />
              </div>

              <div className="d-flex aligns-items-center justify-content-center">
                <input
                  type="submit"
                  className="btn bg-color custom-bg-text"
                  value="Schedule Train"
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

export default ScheduleTrainForm;
