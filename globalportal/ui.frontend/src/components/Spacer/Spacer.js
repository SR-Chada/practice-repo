//Imports
import { MapTo } from "@adobe/aem-react-editable-components";

require("./Spacer.scss");

//Define EditConfig object
/**
 * Default Edit configuration for the Space component
 *
 * @type EditConfig
 */
const SpacerComponentEditConfig = {
  emptyLabel: "Spacer",
  isEmpty: function (props) {
    return !props;
  },
};

const Spacer = (props) => {  
    return (
      <div>{" "}</div>        
    );
  };


//Map the react component to the AEM component using its sling:resourceType
export default MapTo("globalportal/components/spacer")(
    Spacer,
    SpacerComponentEditConfig
  );