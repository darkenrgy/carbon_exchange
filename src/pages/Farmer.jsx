import DashboardLayout from "../components/DashboardLayout";
import DashboardChart from "../components/DashboardChart";
const Farmer = () => {
  return (
    <DashboardLayout>
      {/* Stats */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Total Carbon Credits</p>
          <h2 className="text-2xl font-bold text-green-600">3.1</h2>
        </div>

        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Estimated Earnings</p>
          <h2 className="text-2xl font-bold">₹7,500</h2>
        </div>

        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Organic Status</p>
          <h2 className="text-green-600 font-semibold">✔ Verified</h2>
        </div>
      </div>

      {/* Chart */}
      <div className="bg-white p-6 rounded shadow">
        <h3 className="font-semibold mb-4">
          Carbon Credit & Earnings Growth
        </h3>
        <div className="h-64 flex items-center justify-center text-gray-400">
          <DashboardChart type="line" />
        </div>
      </div>
    </DashboardLayout>
  );
};

export default Farmer;


