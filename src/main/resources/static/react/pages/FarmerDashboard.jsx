import { useEffect, useState } from "react";
import DashboardLayout from "../components/DashboardLayout";
import DashboardChart from "../components/DashboardChart";
import api from "../services/api";

const FarmerDashboard = () => {
  const [stats, setStats] = useState({
    credits: 0,
    earnings: 0,
    organicStatus: "PENDING",
  });

  const [chartData, setChartData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const response = await api.get("/farmer/dashboard");
        setStats(response.data.stats);
        setChartData(response.data.chart);
      } catch (error) {
        console.error("Failed to load farmer dashboard", error);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  if (loading) {
    return (
      <DashboardLayout>
        <p className="text-gray-500">Loading dashboard...</p>
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      {/* Stats */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Total Carbon Credits</p>
          <h2 className="text-2xl font-bold text-green-600">
            {stats.credits}
          </h2>
        </div>

        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Estimated Earnings</p>
          <h2 className="text-2xl font-bold">
            ₹{stats.earnings}
          </h2>
        </div>

        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Organic Status</p>
          <h2 className="text-green-600 font-semibold">
            {stats.organicStatus === "VERIFIED" ? "✔ Verified" : "⏳ Pending"}
          </h2>
        </div>
      </div>

      {/* Chart */}
      <div className="bg-white p-6 rounded shadow">
        <h3 className="font-semibold mb-4">
          Carbon Credit & Earnings Growth
        </h3>

        <div className="h-64">
          <DashboardChart type="line" data={chartData} />
        </div>
      </div>
    </DashboardLayout>
  );
};

export default FarmerDashboard;
