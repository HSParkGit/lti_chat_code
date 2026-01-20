import service from '../api/apiService';
import { MOCK_GROUP_TASKS } from '../config/constants/group';

export const getGroupsService = async () => {
  try {
    const { data, errorCode } = await service.get('system-groups');
    if (errorCode) {
      throw new Error('Failed to fetch groups.');
    }
    return data?.data;
  } catch (error) {
    throw new Error('Failed to fetch groups.');
  }
};

export const getRecentActivityService = async (id) => {
  try {
    const { data, errorCode } = await service.get(`groups/${id}/recent`);
    if (errorCode) {
      throw new Error('Failed to fetch groups.');
    }
    return data?.data;
  } catch (error) {
    throw new Error('Failed to fetch recent activity.');
  }
};

export const getUpComingActivityService = async (id) => {
  try {
    const { data, errorCode } = await service.get(`groups/${id}/coming-up`);
    if (errorCode) {
      throw new Error('Failed to fetch groups.');
    }

    return data?.data;
  } catch (error) {
    throw new Error('Failed to fetch upcoming activity.');
  }
};

export const getAllActivitiesService = async (id) => {
  try {
    const { data, errorCode } = await service.get(`groups/${id}/activities`);
    if (errorCode) {
      throw new Error('Failed to fetch groups.');
    }
    return data?.data;
  } catch (error) {
    throw new Error('Failed to fetch groups.');
  }
};

export const getMockGroupTasks = async () => {
  return new Promise((resolve) => {
    setTimeout(() => {
      return resolve({
        data: MOCK_GROUP_TASKS,
      });
    }, 1000);
  });
};

export const getGroupAgendas = async (groupId) => {
  try {
    const { data, errorCode } = await service.get(`agenda?groupId=${groupId}`);

    if (errorCode) {
      throw new Error();
    }

    return data;
  } catch (error) {
    throw new Error('Failed to fetch group agendas.');
  }
};

export const getGroupThreeDayAgendas = async (groupId) => {
  try {
    const { data, errorCode } = await service.get(`agenda/three-days-calendar?groupId=${groupId}`);

    if (errorCode) {
      throw new Error();
    }

    return data;
  } catch (error) {
    throw new Error('Failed to fetch group agendas.');
  }
};

export const getGroupMembers = async (groupId) => {
  try {
    const { data, errorCode } = await service.get(`group-chat/${groupId}/members`);

    if (errorCode) {
      throw new Error();
    }

    return data;
  } catch (error) {
    throw new Error('Failed to fetch group members.');
  }
};

export const getGroupAnnouncementsService = async (groupId) => {
  if (!groupId) return { data: [] };

  try {
    const { data } = await service.get(`/system-groups/${groupId}/announcements`);
    return data;
  } catch (error) {
    console.error('Error fetching announcements:', error);
    return { data: [] };
  }
};

export const createGroupEventService = async (data) => {
  try {
    const { data: res, errorCode } = await service.post(`system-groups/${data.groupId}/events`, data);
    if (errorCode) {
      throw new Error('Failed to create group event.');
    }
    return res?.data;
  } catch (error) {
    throw new Error('Failed to create group event.');
  }
};

export const editGroupEventService = async (data) => {
  try {
    return await service.put(`system-groups/${data.groupId}/events/${data.eventId}`, data);
  } catch (error) {
    throw new Error('Failed to edit group event.');
  }
};

export const deleteGroupEventService = async (data) => {
  try {
    return await service.delete(`system-groups/${data.groupId}/events/${data.eventId}`);
  } catch (error) {
    throw new Error('Failed to delete group event.');
  }
};

export const createGroupTaskService = async (data) => {
  try {
    const { data: res, errorCode } = await service.post(`agenda`, data);
    if (errorCode) {
      throw new Error('Failed to create group task.');
    }
    return res?.data;
  } catch (error) {
    throw new Error('Failed to create group task.');
  }
};

export const updateGroupTaskService = async (id, data) => {
  try {
    const { data: res, errorCode } = await service.put(`agenda/${id}`, data);
    if (errorCode) {
      throw new Error('Failed to update group task.');
    }
    return res;
  } catch (error) {
    throw new Error('Failed to update group task.');
  }
};

export const updateGroupTasksService = async (data) => {
  try {
    const { data: res, errorCode } = await service.put(`agenda`, data);
    if (errorCode) {
      throw new Error('Failed to update group tasks.');
    }
    return res;
  } catch (error) {
    throw new Error('Failed to update group tasks.');
  }
};

export const getAllGroupsByAdminService = async () => {
  try {
    const { data, errorCode } = await service.get(`group-chat/admin`);
    if (errorCode) {
      throw new Error('Failed to fetch groups.');
    }
    return data?.data;
  } catch (error) {
    throw new Error('Failed to fetch groups.');
  }
};

export const getAllGroupsService = async () => {
  try {
    const { data, errorCode } = await service.get(`group-chat`);
    if (errorCode) {
      throw new Error('Failed to fetch groups.');
    }
    return data?.data;
  } catch (error) {
    throw new Error('Failed to fetch groups.');
  }
};

export const updateGroup = async (data) => {
  try {
    const { data: res, errorCode } = await service.put(`group-chat/${data?.groupId}`, data);
    if (errorCode) {
      throw new Error('Failed to update group.');
    }
    return res?.data;
  } catch (error) {
    throw new Error('Failed to update group.');
  }
};

export const deleteGroup = async (groupId) => {
  try {
    const { data, errorCode } = await service.delete(`group-chat/${groupId}`);
    if (errorCode) {
      throw new Error('Failed to delete group.');
    }
    return data?.data;
  } catch (error) {
    throw new Error('Failed to delete group.');
  }
};

export const getGroupEvents = async ({ groupId, startDate, endDate }) => {
  try {
    const { data, errorCode } = await service.get(
      `system-groups/${groupId}/events?startDate=${startDate}&endDate=${endDate}`
    );
    if (errorCode) {
      throw new Error('Failed to fetch group events.');
    }
    return data;
  } catch (error) {
    throw new Error('Failed to fetch group events.');
  }
};

export const getGroupMemberProgress = async ({ groupId, range, month }) => {
  try {
    const { data, errorCode } = await service.get(`groups/${groupId}/member-report`, {
      params: {
        range,
        month,
      },
    });
    if (errorCode) {
      throw new Error('Failed to fetch group member progress.');
    }
    return data?.data;
  } catch (error) {
    throw new Error('Failed to fetch group member progress.');
  }
};

export const getInviteLink = async (groupId) => {
  try {
    const { data, errorCode } = await service.get(`group-chat/${groupId}/invite-link`);
    if (errorCode) {
      throw new Error('Failed to fetch invite link.');
    }
    return data?.data;
  } catch (error) {
    throw new Error('Failed to fetch invite link.');
  }
};

export const acceptGroupInvite = async (inviteCode) => {
  try {
    return await service.post(`/group-chat/join?${inviteCode}`);
  } catch (e) {
    throw new Error(e);
  }
};
