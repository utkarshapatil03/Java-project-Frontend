import AdminHeader from "./AdminHeader";
import NormalHeader from "./NormalHeader";
import CustomerHeader from "./CustomerHeader";

const RoleNav = () => {
  const customer = JSON.parse(sessionStorage.getItem("active-customer"));
  const admin = JSON.parse(sessionStorage.getItem("active-admin"));

  if (admin != null) {
    return <AdminHeader />;
  } else if (customer != null) {
    return <CustomerHeader />;
  } else {
    return <NormalHeader />;
  }
};

export default RoleNav;
