import DashboardLayout from "../components/DashboardLayout";
import DashboardChart from "../components/DashboardChart";

const Company = () => {
  return (
    <DashboardLayout>
      {/* Stats */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Emission This Year</p>
          <h2 className="text-2xl font-bold text-red-600">1,200 tCO₂</h2>
        </div>

        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Credits Required</p>
          <h2 className="text-2xl font-bold">320</h2>
        </div>

        <div className="bg-white p-4 rounded shadow">
          <p className="text-gray-500">Avg Credit Price</p>
          <h2 className="text-2xl font-bold">₹2,600</h2>
        </div>
      </div>

      {/* Chart Placeholder */}
      <div className="bg-white p-6 rounded shadow">
        <h3 className="font-semibold mb-4">Carbon Credit Market Trend</h3>
        <div className="h-64 flex items-center justify-center text-gray-400">
          <DashboardChart type="bar" />
        </div>
      </div>
    </DashboardLayout>
  );
};

export default Company;
