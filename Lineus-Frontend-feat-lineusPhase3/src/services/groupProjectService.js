import service from '@/api/apiService';
import { error } from 'jquery';

export const groupProjectService = {
  createGroupProject: async (projectData) => {
    try {
      const res = await service.post('group-projects', projectData);
      return res.data;
    } catch (error) {
      console.error('Error creating group project:', error);
      throw error;
    }
  },

  getGroupProjectLists: async ({ projectId, pageNumber }) => {
    try {
      const { data } = await service.get(`group-projects/groups/list-by-group-project-id/${projectId}`, {
        params: {
          page: pageNumber,
        },
      });
      return data?.data;
    } catch (error) {
      console.error('Error creating group project:', error);
      throw error;
    }
  },

  getGroupProjectListsByPhaseId: async ({ phaseId, pageNumber, size }) => {
    try {
      const { data } = await service.get(
        `group-projects/groups/list-by-phase-id/${phaseId}?page=${pageNumber}&size=${size}`
      );
      return data?.data;
    } catch (error) {
      console.error('Error creating group project:', error);
      throw error;
    }
  },

  updateGroupProject: async (formData) => {
    try {
      const { data } = await service.patch(`group-projects/groups/${formData?.groupId}`, formData);
      return data?.data?.content;
    } catch (error) {
      console.error('Error creating group project:', error);
      throw error;
    }
  },

  deleteGroupProject: async (groupId) => {
    try {
      const { data } = await service.delete(`group-projects/groups/${groupId}`);
      return data?.data?.content;
    } catch (error) {
      console.error('Error deleting group project:', error);
      throw error;
    }
  },

  deleteAllGroupProjects: async (projectId) => {
    try {
      const { data } = await service.delete(`group-projects/groups/${projectId}/delete-all`);
      return data;
    } catch (error) {
      console.error('Error deleting multiple group projects:', error);
      throw error;
    }
  },

  uploadCsv: async (formData) => {
    try {
      const res = await service.post(
        `group-projects/groups/${formData.projectId}/phases/${formData.phaseId}`,
        formData.formData
      );
      return res;
    } catch (error) {
      console.error('Error deleting multiple group projects:', error);
      throw error;
    }
  },

  removeMember: async ({ groupId, memberId }) => {
    try {
      const res = await service.delete(`group-projects/groups/${groupId}/members/${memberId}`);
      return res;
    } catch (error) {
      console.error('Error deleting multiple group projects:', error);
      throw error;
    }
  },

  saveGroupLists: async (formData) => {
    try {
      const res = await service.post(`group-projects/groups/save`, formData);
      return res;
    } catch (error) {
      console.error('Error deleting multiple group projects:', error);
      throw error;
    }
  },

  moveMemberToAnotherGroup: async (formData) => {
    try {
      const res = await service.post(`group-projects/groups/save`, formData);
      return res;
    } catch (error) {
      console.error('Error deleting multiple group projects:', error);
      throw error;
    }
  },

  addNewMember: async (formData) => {
    try {
      const res = await service.post(`group-projects/groups/${formData.groupId}/members/add`, formData);
      return res;
    } catch (error) {
      console.error('Error deleting multiple group projects:', error);
      throw error;
    }
  },

  createAutoGenerateGroup: async (formData) => {
    try {
      const { data, errorMessage } = await service.post(`group-projects/groups/create-auto-generate`, formData);
      if (errorMessage) {
        Promise.reject();
        return { errorMessage };
      }
      return data;
    } catch (error) {
      console.error('Error deleting multiple group projects:', error);
      throw error;
    }
  },

  getGroupSettings: async (groupProjectId) => {
    try {
      const { data } = await service.get(`group-projects/groups/settings`, {
        params: { groupProjectId },
      });

      return data?.data;
    } catch (error) {
      console.error('Error creating group project:', error);
      throw error;
    }
  },

  updateGroupSettings: async (formData) => {
    try {
      const res = await service.put(`group-projects/groups/settings/${formData.projectId}`, formData);
      return res;
    } catch (error) {
      console.error('Error deleting multiple group projects:', error);
      throw error;
    }
  },

  createProjectGroup: async (formData) => {
    try {
      const res = await service.post('group-projects/groups', formData);
      return res;
    } catch (error) {
      console.error('Error deleting multiple group projects:', error);
      throw error;
    }
  },

  copyPreviousPhaseGroups: async (formData) => {
    try {
      const { data, errorCode } = await service.post('group-projects/groups/copy-from-past-phase', formData);
      if (errorCode) throw error;
      return data;
    } catch (error) {
      console.error('Error deleting multiple group projects:', error);
      throw error;
    }
  },

  getAllStudent: async (formData) => {
    try {
      const { data, errorCode } = await service.get(
        `group-projects/groups/students?withoutGroup=${formData.without_group}&page=${formData.pageNumber}&size=${formData.size}&keyword=${formData.keyword}&courseId=${formData.courseId}`
      );
      if (errorCode) throw error;
      return data?.data;
    } catch (error) {
      console.error('Error deleting multiple group projects:', error);
      throw error;
    }
  },

  leaderShipManagement: async ({ groupId, memberId }) => {
    try {
      const res = await service.post(`group-projects/groups/${groupId}/members/${memberId}/leadership`);
      return res;
    } catch (error) {
      console.error('Error deleting multiple group projects:', error);
      throw error;
    }
  },

  getStudentListsByGroupId: async (groupId) => {
    try {
      const { data } = await service.get(`group-projects/groups/${groupId}/students?page=1`);

      return data?.data?.content;
    } catch (error) {
      console.error('Error deleting multiple group projects:', error);
      throw error;
    }
  },

  assignGroups: async (formData) => {
    try {
      const res = await service.post('group-projects/groups/assign-groups-to-phase', formData);
      return res;
    } catch (error) {
      console.error('Error deleting multiple group projects:', error);
      throw error;
    }
  },
};
