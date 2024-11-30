import Header from "../header";

export default function Layout({ children }){
  return(
    <div className="layout">
      <Header/>
      {children}
    </div>
  )
}