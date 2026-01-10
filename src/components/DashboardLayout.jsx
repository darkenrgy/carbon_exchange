import Navbar from "./navbar";
import Sidebar from "./sidebar";

const DashboardLayout = ({ children }) => {
  return (
    <div className="min-h-screen flex flex-col">
    <div classname="p-h">
      {/* Navbar */}
      <Navbar />
      </div>

      <div className="flex flex-1">
        {/* Sidebar */}
        <Sidebar />

        {/* Main Content */}
        <div className="flex-1 p-6 bg-green-100 overflow-y-auto">
          {children}
        </div>
      </div>
    </div>
  );
};

export default DashboardLayout;
