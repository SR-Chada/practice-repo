import { Page, withModel } from "@adobe/aem-react-editable-components";
import React from "react";

// This component is the application entry point
class App extends Page {
  componentDidMount() {
    const element = document.getElementsByTagName("body")[0];
    element.classList.remove("loader");

    setTimeout(() => {
      if (window?.document) {
        const arr = document.getElementsByClassName("cmp-teaser__action-link");
        for (let index = 0; index < arr.length; index++) {
          const replacedDollor = arr[index].innerText.replace("$", "");
          arr[index].innerHTML = replacedDollor;
        }

        const arr2 = document.getElementsByClassName("full-cmp-width");
        for (let index = 0; index < arr2.length; index++) {
          const element = arr2[index];
          const contDiv = element.firstChild;
          const cmpContDiv = contDiv.firstChild;
          const aemContDiv = cmpContDiv.firstChild;
          const actualCompDiv = aemContDiv.getElementsByClassName(
            "cmp-global-teaser-list-variation-one"
          );
          if (actualCompDiv && actualCompDiv.length > 0) {
            element.style.padding = 0;
          }
        }

      }
    }, 1500);
    setTimeout(() => {
      const arr1 = document.getElementsByClassName("cmp-global-teaser-default");
      for (let index = 0; index < arr1.length; index++) {
        const DDImageCont = arr1[index].firstChild;
        const imageCont = DDImageCont.firstChild;
        const contentCont = DDImageCont.lastChild;
        const titleDiv =

          contentCont?.getElementsByClassName("cmp-teaser__title");
          if(titleDiv != undefined && titleDiv.length > 0){
            imageCont.style.paddingTop = titleDiv[0]?.clientHeight + 50 + "px";
          }
       }

    }, 1000);
    setTimeout(() => {
      const selectRegionId = document.getElementById("select-region");
      if (selectRegionId) {
        localStorage.setItem("select-region", selectRegionId?.href);
      }
    }, 500);
    setTimeout(() => {
      const forInvestors = document.getElementById("for-investors");
      if (forInvestors) {
        localStorage.setItem("for-investors", forInvestors?.href);
      }
    }, 500);
    setTimeout(() => {
      const arr2 = document.getElementsByClassName("full-cmp-width");
      for (let index = 0; index < arr2.length; index++) {
        const element = arr2[index];
        const contDiv = element.firstChild;
        const cmpContDiv = contDiv.firstChild;
        const aemContDiv = cmpContDiv.firstChild;
        const actualCompDiv = aemContDiv.getElementsByClassName(
          "cmp-global-teaser-list-variation-one"
        );
        if (actualCompDiv && actualCompDiv.length > 0) {
          element.style.padding = 0;
        }
      }
    }, 1000);
  }
  render() {
    return (
      <div>
        {this.childPages.length > 0 ? this.childPages : this.childComponents}
      </div>
    );
  }
}

export default withModel(App);
