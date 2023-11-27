import { MapTo } from "@adobe/aem-react-editable-components";
import { useEffect, useState } from "react";
import PageSectionListing from "../PageSectionListing/PageSectionListing";
import { monthNames } from "../Common/Common";
require("./Milestones.scss");

const MilestonesEditConfig = {
  emptyLabel: "Milestones",
  isEmpty: function (props) {
    return !props;
  },
};
const Milestones = (props) => {

  const getData = (date) => {
    var d = JSON.stringify(date).split("-");
    return { month: d[1] - 1, year: d[0].slice(1) };
  };

  const isOdd = (num) => !(num % 2);

  const [mileStoneItems, setMileStoneItems] = useState([]);
  const [listItems, setListItems] = useState([]);
  useEffect(() => {
    if (props.itemsMap) {
      const toArray = Object.entries(props.itemsMap);
      setMileStoneItems(toArray);
      setListItems(() => {
        return toArray.map((item) => ({
          itemLabel: item[0],
          itemLink: {
            valid: true,
            attributes: {
              href: `#${item[0]}`,
            },
            url: `/${item[0]}`,
          },
          itemHashLink: `#${item[0]}`,
        }));
      });
    }
  }, []);

  return (
    <div className="milestones-main-container">
      <PageSectionListing listItems={listItems} />
      <div className="milestones-container">
        {mileStoneItems.length > 0 &&
          mileStoneItems.map((item, i) => {
            item[1].sort((a, b) => new Date(a.formattedDate) - new Date(b.formattedDate));
            return (
              <div
                key={item[0]}
                className={`milestones-outer-container ${
                  !isOdd(i) ? "with-bg" : ""
                }`}
                id={item[0]}
              >
                <div
                  className={`max-width-inner-container ${i == 0 ? "pt0" : ""}`}
                >
                  <h3>{item[0]}</h3>
                  {item[1].length > 0 &&
                    item[1].map((milestoneItem, itemNumber) => (
                      <div className="milestones-inner-container">
                        <h4>
                          {monthNames[
                            getData(milestoneItem?.formattedDate).month
                          ] +
                            " " +
                            getData(milestoneItem?.formattedDate).year}
                        </h4>

                        <div
                          className={`desc ${
                            itemNumber == item[1].length - 1 ? "pb0" : ""
                          }`}
                          dangerouslySetInnerHTML={{
                            __html: milestoneItem?.milestoneDescription,
                          }}
                        />
                      </div>
                    ))}
                </div>
              </div>
            );
          })}
      </div>
    </div>
  );
};
//Map the react component to the AEM component using its sling:resourceType
export default MapTo("globalportal/components/milestones")(
  Milestones,
  MilestonesEditConfig
);
