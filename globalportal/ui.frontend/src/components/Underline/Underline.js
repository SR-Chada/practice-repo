//Imports
import { MapTo } from "@adobe/aem-react-editable-components";

require("./Underline.scss");

//Define EditConfig object
/**
 * Default Edit configuration for the Space component
 *
 * @type EditConfig
 */
const UnderlineComponentEditConfig = {
  emptyLabel: "Underline",
  isEmpty: function (props) {
    return !props;
  },
};

const Underline = () => {  
    return (
      <div>{" "}</div>        
    );
  };


//Map the react component to the AEM component using its sling:resourceType
export default MapTo("globalportal/components/underline")(
    Underline,
    UnderlineComponentEditConfig
  );