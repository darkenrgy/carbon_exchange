const Navbar = () => {
  const role = localStorage.getItem("role");

  const logout = () => {
    localStorage.clear();
    window.location.href = "/";
  };

  return (
    <div className="h-20 bg-green-700 text-white flex items-center justify-between px-6">
      <h1 className="font-bold text-lg">
        Carbon Exchange Platform
      </h1>

      <div className="flex items-center gap-4">
        <span className="capitalize">{role} Dashboard</span>
        <button
          onClick={logout}
          className="bg-red-500 px-3 py-1 rounded"
        >
          Logout
        </button>
      </div>
    </div>
  );
};

export default Navbar;
