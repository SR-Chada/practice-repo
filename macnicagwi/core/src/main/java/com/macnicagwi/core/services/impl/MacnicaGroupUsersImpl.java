package com.macnicagwi.core.services.impl;

import com.macnicagwi.core.services.MacnicaGroupUsers;
import com.macnicagwi.core.utils.MacnicaCoreUtils;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.jcr.Value;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.MACNICA_SUB_SERVICE;

@Component(service = MacnicaGroupUsers.class, immediate = true)
@ServiceDescription("Macnica Group Users Service")
public class MacnicaGroupUsersImpl implements MacnicaGroupUsers {

    private static final Logger LOGGER = LoggerFactory.getLogger(MacnicaGroupUsersImpl.class);
    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    /**
     * To get the email of the groups
     *
     * @param id group id/name
     * @return list of emails
     */
    @Override
    public List<Value> getUserDetails(String id) {
        LOGGER.trace("----------- inside getUserDetails ----------> {} ", id);
        ResourceResolver serviceResolver = MacnicaCoreUtils.getResourceResolver(resourceResolverFactory,
                MACNICA_SUB_SERVICE);
        LOGGER.trace("serviceResolver = {}", serviceResolver);
        Session session = serviceResolver.adaptTo(Session.class);
        LOGGER.trace("------------ session = {} -----------", session);
        Value[] usersEmail;
        List<Value> usersEmailList = new ArrayList<>();
        try {
            JackrabbitSession jrSession = (JackrabbitSession) session;
            LOGGER.trace("------------ jrSession = {} ---------", jrSession);
            UserManager userManager;
            final Iterator<Authorizable> memberslist;
            userManager = jrSession.getUserManager();
            LOGGER.trace("------------ userManager = {} ---------", userManager);
            Group group = (Group) userManager.getAuthorizable(id);
            LOGGER.trace("------------ group = {} ---------", group);
            memberslist = group.getMembers();
            LOGGER.trace(" ---- memberslist --> {}", memberslist);
            while (memberslist.hasNext()) {
                final Authorizable user = memberslist.next();
                LOGGER.trace("------------ Authorizable user = {} ---------", user);
                if (user != null && !user.isGroup() && user.getProperty("./profile/email") != null) {
                    // EMAIL ADDRESS FOR MEMBERS
                    usersEmail = user.getProperty("./profile/email");
                    LOGGER.trace("------------ usersEmail = {} ---------", usersEmail);
                    for (Value email : usersEmail) {
                        LOGGER.trace("------------ email = {} ---------", email);
                        usersEmailList.add(email);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
        }
        return usersEmailList;
    }

    @Override
    public List<Value> getUserEmail(String id) {
        ResourceResolver serviceResolver = MacnicaCoreUtils.getResourceResolver(resourceResolverFactory,
                MACNICA_SUB_SERVICE);
        Session session = serviceResolver.adaptTo(Session.class);
        Value[] usersEmail;
        List<Value> usersEmailList = new ArrayList<Value>();
        try {
            JackrabbitSession jrSession = (JackrabbitSession) session;
            UserManager userManager = null;
            userManager = jrSession.getUserManager();
            final Authorizable user = userManager.getAuthorizable(id);
            if (user != null && !user.isGroup() && user.getProperty("./profile/email") != null) {
                usersEmail = user.getProperty("./profile/email");
                for (Value email : usersEmail) {
                    usersEmailList.add(email);
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return usersEmailList;
    }



}
