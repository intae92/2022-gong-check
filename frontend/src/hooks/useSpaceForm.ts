import useToast from './useToast';
import { AxiosError } from 'axios';
import { useMutation } from 'react-query';
import { useNavigate } from 'react-router-dom';

import apiSpace from '@/apis/space';

import { ID } from '@/types';

const useSpaceForm = () => {
  const navigate = useNavigate();

  const { openToast } = useToast();

  const { mutate: createSpace } = useMutation(
    ({ name, imageUrl }: { name: string; imageUrl: string }) => apiSpace.postNewSpace(name, imageUrl),
    {
      onSuccess: res => {
        const locationSplitted = res.headers.location.split('/');
        const spaceId: ID = locationSplitted[locationSplitted.length - 1];

        openToast('SUCCESS', '공간이 생성 되었습니다.');
        navigate(`/host/manage/${spaceId}`);
      },
      onError: (err: AxiosError<{ message: string }>) => {
        openToast('ERROR', `${err.response?.data.message}`);
      },
    }
  );

  const { mutate: updateSpace } = useMutation(
    ({ spaceId, name, imageUrl }: { spaceId: ID; name: string; imageUrl: string }) =>
      apiSpace.putSpace(spaceId, name, imageUrl),
    {
      onSuccess: (_, { spaceId }) => {
        openToast('SUCCESS', '공간 정보가 수정 되었습니다.');
        navigate(`/host/manage/${spaceId}`);
      },
      onError: (err: AxiosError<{ message: string }>) => {
        openToast('ERROR', `${err.response?.data.message}`);
      },
    }
  );

  const onSubmitCreateSpace = (e: React.FormEvent<HTMLFormElement>, imageUrl: string) => {
    e.preventDefault();
    const form = e.target as HTMLFormElement;
    const name = form['nameInput'].value;

    createSpace({ name, imageUrl });
  };

  const onSubmitUpdateSpace = async (e: React.FormEvent<HTMLFormElement>, imageUrl: string, spaceId: ID) => {
    e.preventDefault();
    const form = e.target as HTMLFormElement;
    const name = form['nameInput'].value;

    updateSpace({ spaceId, name, imageUrl });
  };

  return { onSubmitCreateSpace, onSubmitUpdateSpace };
};

export default useSpaceForm;
