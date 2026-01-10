import DashboardLayout from "../components/DashboardLayout";
import DashboardChart from "../components/DashboardChart";


const Admin = () => {
  return (
    <DashboardLayout>
      {/* Stats */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Total Farmers</p>
          <h2 className="text-2xl font-bold text-green-700">248</h2>
        </div>

        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Total Companies</p>
          <h2 className="text-2xl font-bold">54</h2>
        </div>

        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Pending Verifications</p>
          <h2 className="text-2xl font-bold text-orange-500">19</h2>
        </div>

        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Credits Traded</p>
          <h2 className="text-2xl font-bold">12,800</h2>
        </div>
      </div>

      {/* Table */}
      <div className="bg-white p-6 rounded shadow">
        <h3 className="font-semibold mb-4">Pending Farmer Approvals</h3>

        <table className="w-full border">
          <thead className="bg-gray-100">
            <tr>
              <th className="p-2 border">Farmer</th>
              <th className="p-2 border">Land Area</th>
              <th className="p-2 border">Status</th>
              <th className="p-2 border">Action</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td className="p-2 border">Ramesh Kumar</td>
              <td className="p-2 border">2.5 ha</td>
              <td className="p-2 border text-orange-500">Pending</td>
              <td className="p-2 border">
                <button className="bg-green-600 text-white px-3 py-1 rounded mr-2">
                  Approve
                </button>
                <button className="bg-red-500 text-white px-3 py-1 rounded">
                  Reject
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </DashboardLayout>
  );
};

export default Admin;

