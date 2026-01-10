import { useEffect, useState } from "react";
import DashboardLayout from "../components/DashboardLayout";
import DashboardChart from "../components/DashboardChart";
import api from "../services/api";

const CompanyDashboard = () => {
  const [stats, setStats] = useState({
    yearlyEmission: 0,
    creditsRequired: 0,
    avgCreditPrice: 0,
  });

  const [chartData, setChartData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchCompanyDashboard = async () => {
      try {
        const response = await api.get("/company/dashboard");
        setStats(response.data.stats);
        setChartData(response.data.chart);
      } catch (error) {
        console.error("Failed to load company dashboard", error);
      } finally {
        setLoading(false);
      }
    };

    fetchCompanyDashboard();
  }, []);

  if (loading) {
    return (
      <DashboardLayout>
        <p className="text-gray-500">Loading company dashboard...</p>
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      {/* Stats */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Emission This Year</p>
          <h2 className="text-2xl font-bold text-red-600">
            {stats.yearlyEmission} tCO₂
          </h2>
        </div>

        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Credits Required</p>
          <h2 className="text-2xl font-bold">
            {stats.creditsRequired}
          </h2>
        </div>

        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Avg Credit Price</p>
          <h2 className="text-2xl font-bold">
            ₹{stats.avgCreditPrice}
          </h2>
        </div>
      </div>

      {/* Chart */}
      <div className="bg-white p-6 rounded shadow">
        <h3 className="font-semibold mb-4">
          Carbon Credit Market Trend
        </h3>

        <div className="h-64">
          <DashboardChart type="bar" data={chartData} />
        </div>
      </div>
    </DashboardLayout>
  );
};

export default CompanyDashboard;
