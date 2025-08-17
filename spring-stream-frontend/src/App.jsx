import { useState } from "react";
import "./App.css";
import VideoUpload from "./components/VideoUpload";
import { Toaster } from "react-hot-toast";
import VideoPlayer from "./components/VideoPlayer";
import { Button, TextInput } from "flowbite-react";

function App() {
  const [count, setCount] = useState(0);
  const [fieldValue, setFieldValue] = useState(null);
  const [videoId, setVideoId] = useState("");

  function playVideo(videoId) {
    setVideoId(videoId);
  }

  return (
    <>
      <Toaster />
      <div className="flex flex-col items-center space-y-9 justify-center py-9">
        <h1 className="text-2xl font-bold text-gray-700 dark:text-gray-100">
          Video Streaming App
        </h1>

        <div className="flex mt-14 w-full space-x-2 justify-between">
          <div className="w-full">
            <h1 className="text-white text-center mt-2">Playing Video</h1>

            <div>
              <VideoPlayer
                src={`http://localhost:8080/api/v1/videos/${videoId}/master.m3u8`}
              />
            </div>
          </div>

          <div className="w-full">
            <VideoUpload />
          </div>
        </div>

        <div className="my-4 flex space-x-4">
          <TextInput
            onChange={(event) => {
              setFieldValue(event.target.value);
            }}
            placeholder="Enter video id here"
            name="video_id_field"
          />

          <Button
            onClick={() => {
              if (!fieldValue) {
                alert("Please enter a video ID");
                return;
              }
              setVideoId(fieldValue);
            }}
          >
            Play
          </Button>
        </div>
      </div>
    </>
  );
}

export default App;
