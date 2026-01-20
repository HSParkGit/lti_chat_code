import { fn } from '@storybook/test';
import IconButton from './IconButton';
import { BsAirplaneEnginesFill } from 'react-icons/bs';

export default {
  title: 'Example/IconButton',
  component: IconButton,
  parameters: {
    layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {
    backgroundColor: { control: 'color' },
  },
  args: { onClick: fn() },
};

export const Rounded = {
  args: {
    color: 'primary',
    size: 'md',
    className: '!rounded-full',
  },
  render: (args) => (
    <IconButton {...args}>
      <BsAirplaneEnginesFill />
    </IconButton>
  ),
};

export const Rect = {
  args: {
    color: 'primary',
    size: 'md',
  },
  render: (args) => (
    <IconButton {...args}>
      <BsAirplaneEnginesFill />
    </IconButton>
  ),
};

export const DangerColor = {
  args: {
    color: 'danger',
    size: 'md',
    className: '!rounded-full',
  },
  render: (args) => (
    <IconButton {...args}>
      <BsAirplaneEnginesFill />
    </IconButton>
  ),
};

export const CustomColor = {
  args: {
    color: 'orange',
    size: 'md',
    className: '!rounded-full',
  },
  render: (args) => (
    <IconButton {...args}>
      <BsAirplaneEnginesFill />
    </IconButton>
  ),
};
