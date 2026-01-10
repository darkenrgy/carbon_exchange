import {
  BarChart,
  Bar,
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

const barData = [
  { month: "Jan", credits: 120 },
  { month: "Feb", credits: 180 },
  { month: "Mar", credits: 260 },
  { month: "Apr", credits: 310 },
];

const lineData = [
  { month: "Jan", earnings: 2000 },
  { month: "Feb", earnings: 3500 },
  { month: "Mar", earnings: 5200 },
  { month: "Apr", earnings: 7500 },
];

const DashboardChart = ({ type }) => {
  return (
    <div className="bg-white rounded-xl shadow-md p-4 h-72">
      <h3 className="font-semibold mb-3">
        {type === "bar"
          ? "Carbon Credit Market Trend"
          : "Carbon Credit & Earnings Growth"}
      </h3>

      <ResponsiveContainer width="100%" height="100%">
        {type === "bar" ? (
          <BarChart data={barData}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="month" />
            <YAxis />
            <Tooltip />
            <Bar dataKey="credits" />
          </BarChart>
        ) : (
          <LineChart data={lineData}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="month" />
            <YAxis />
            <Tooltip />
            <Line
              type="monotone"
              dataKey="earnings"
              strokeWidth={3}
            />
          </LineChart>
        )}
      </ResponsiveContainer>
    </div>
  );
};

export default DashboardChart;
