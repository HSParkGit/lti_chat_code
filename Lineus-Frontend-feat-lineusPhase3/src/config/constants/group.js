export const MOCK_GROUP_TASKS = [
  {
    id: 1,
    name: 'Samule',
    agenda: [
      { id: 1, priority: 'MEDIUM', title: 'Infoo 360 lecture', completed: false },
      { id: 2, priority: 'HIGH', title: 'HCE Leture 12', completed: true },
      { id: 3, priority: 'HIGH', title: 'Zoom call with mse team for projects', completed: false },
      { id: 4, priority: 'LOW', title: 'A4 DUE', completed: false },
    ],
  },
  {
    id: 2,
    name: 'Peter',
    agenda: [
      { id: 1, priority: 'MEDIUM', title: 'Infoo 360 lecture', completed: false },
      { id: 2, priority: 'HIGH', title: 'HCE Leture 12', completed: true },
      { id: 3, priority: 'HIGH', title: 'Zoom call with mse team for projects', completed: false },
      { id: 4, priority: 'LOW', title: 'A4 DUE', completed: false },
    ],
  },
  {
    id: 3,
    name: 'David',
    agenda: [
      { id: 1, priority: 'MEDIUM', title: 'Infoo 360 lecture', completed: false },
      { id: 2, priority: 'HIGH', title: 'HCE Leture 12', completed: true },
      { id: 3, priority: 'LOW', title: 'A4 DUE', completed: false },
    ],
  },
  {
    id: 4,
    name: 'Mihn Tu',
    agenda: [
      { id: 1, priority: 'MEDIUM', title: 'Infoo 360 lecture', completed: false },
      { id: 2, priority: 'HIGH', title: 'HCE Leture 12', completed: true },
    ],
  },
];

export const PRIORITIES = {
  LOW: 'LOW',
  MEDIUM: 'MEDIUM',
  HIGH: 'HIGH',
};

export const PRIORITY_COLORS = {
  LOW: '0, 122, 255',
  MEDIUM: '255, 149, 0',
  HIGH: '255, 45, 85',
};
