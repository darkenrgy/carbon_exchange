import { useEffect, useState } from "react";
import DashboardLayout from "../components/DashboardLayout";
import api from "../services/api";

const AdminDashboard = () => {
  const [stats, setStats] = useState({
    totalFarmers: 0,
    totalCompanies: 0,
    pendingVerifications: 0,
    creditsTraded: 0,
  });

  const [pendingFarmers, setPendingFarmers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAdminDashboard = async () => {
      try {
        const response = await api.get("/admin/dashboard");
        setStats(response.data.stats);
        setPendingFarmers(response.data.pendingFarmers);
      } catch (error) {
        console.error("Failed to load admin dashboard", error);
      } finally {
        setLoading(false);
      }
    };

    fetchAdminDashboard();
  }, []);

  const handleApprove = async (farmerId) => {
    await api.post(`/admin/farmer/${farmerId}/approve`);
    setPendingFarmers(pendingFarmers.filter(f => f.id !== farmerId));
  };

  const handleReject = async (farmerId) => {
    await api.post(`/admin/farmer/${farmerId}/reject`);
    setPendingFarmers(pendingFarmers.filter(f => f.id !== farmerId));
  };

  if (loading) {
    return (
      <DashboardLayout>
        <p className="text-gray-500">Loading admin dashboard...</p>
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      {/* Stats */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Total Farmers</p>
          <h2 className="text-2xl font-bold text-green-700">
            {stats.totalFarmers}
          </h2>
        </div>

        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Total Companies</p>
          <h2 className="text-2xl font-bold">
            {stats.totalCompanies}
          </h2>
        </div>

        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Pending Verifications</p>
          <h2 className="text-2xl font-bold text-orange-500">
            {stats.pendingVerifications}
          </h2>
        </div>

        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Credits Traded</p>
          <h2 className="text-2xl font-bold">
            {stats.creditsTraded}
          </h2>
        </div>
      </div>

      {/* Pending Farmers Table */}
      <div className="bg-white p-6 rounded shadow">
        <h3 className="font-semibold mb-4">Pending Farmer Approvals</h3>

        <table className="w-full border">
          <thead className="bg-gray-100">
            <tr>
              <th className="p-2 border">Farmer</th>
              <th className="p-2 border">Land Area (ha)</th>
              <th className="p-2 border">Status</th>
              <th className="p-2 border">Action</th>
            </tr>
          </thead>
          <tbody>
            {pendingFarmers.length === 0 ? (
              <tr>
                <td colSpan="4" className="p-4 text-center text-gray-500">
                  No pending approvals 🎉
                </td>
              </tr>
            ) : (
              pendingFarmers.map((farmer) => (
                <tr key={farmer.id}>
                  <td className="p-2 border">{farmer.name}</td>
                  <td className="p-2 border">{farmer.landArea}</td>
                  <td className="p-2 border text-orange-500">Pending</td>
                  <td className="p-2 border">
                    <button
                      onClick={() => handleApprove(farmer.id)}
                      className="bg-green-600 text-white px-3 py-1 rounded mr-2"
                    >
                      Approve
                    </button>
                    <button
                      onClick={() => handleReject(farmer.id)}
                      className="bg-red-500 text-white px-3 py-1 rounded"
                    >
                      Reject
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </DashboardLayout>
  );
};

export default AdminDashboard;
